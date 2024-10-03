package io.hhplus.arch.application.lecture.facade;

import io.hhplus.arch.application.lecture.dto.*;
import io.hhplus.arch.application.lecture.service.LectureEnrollService;
import io.hhplus.arch.application.lecture.service.LectureScheduleService;
import io.hhplus.arch.application.lecture.service.LectureService;
import io.hhplus.arch.application.user.service.UserService;
import io.hhplus.arch.common.transaction.FunctionalTransaction;
import io.hhplus.arch.common.transaction.TransactionItem;
import io.hhplus.arch.domain.lecture.entity.Lecture;
import io.hhplus.arch.domain.lecture.entity.LectureEnroll;
import io.hhplus.arch.domain.lecture.entity.LectureSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LectureFacade {
    private final FunctionalTransaction functionalTransaction;
    private final LectureService lectureService;
    private final LectureScheduleService lectureScheduleService;
    private final LectureEnrollService lectureEnrollService;
    private final UserService userService;

    public LectureFacade(
            FunctionalTransaction functionalTransaction,
            LectureService lectureService,
            LectureScheduleService lectureScheduleService,
            LectureEnrollService lectureEnrollService,
            UserService userService
    ) {
        this.lectureService = lectureService;
        this.lectureScheduleService = lectureScheduleService;
        this.lectureEnrollService = lectureEnrollService;
        this.userService = userService;
        this.functionalTransaction = functionalTransaction;
    }

    public Page<LectureDTO> getLectureAvailableAll(
            final Pageable pageable,
            final LocalDate startDate,
            final LocalDate endDate
    ) {
        return functionalTransaction.perform(new TransactionItem<>(true, TransactionDefinition.ISOLATION_READ_COMMITTED,
                handler -> {

            List<Long> availableLectureIds = lectureScheduleService.getAvailableLectureIds(startDate, endDate);
            Page<Lecture> lectures = lectureService.getLectureAllIn(
                    availableLectureIds,
                    pageable
            );
            List<Long> targetLectureIds = lectures.getContent().stream().map(Lecture::getId).toList();
            Map<Long, List<LectureSchedule>> schedules = lectureScheduleService.getMapAvailableByLectureIdIn(targetLectureIds);

            List<LectureDTO> lectureDTOs = lectures.getContent().stream()
                    .map(lecture -> {
                        List<LectureScheduleDTO> scheduleDTOs = schedules.getOrDefault(lecture.getId(), List.of())
                                .stream()
                                .map(schedule -> new LectureScheduleDTO(
                                        schedule.getId(),
                                        schedule.getLectureDate(),
                                        schedule.getCapacity())
                                )
                                .collect(Collectors.toList());

                        return new LectureDTO(
                                lecture.getId(),
                                lecture.getTitle(),
                                lecture.getLecturerName(),
                                scheduleDTOs
                        );
                    })
                    .toList();

            return new PageImpl<>(lectureDTOs, pageable, lectures.getTotalElements());
        }));
    }

    public LectureEnrollDTO enroll(
            final Long lectureScheduleId,
            final Long userId
    ) {
        return functionalTransaction.perform(new TransactionItem<>(TransactionDefinition.ISOLATION_READ_COMMITTED,
                handler -> {

            LectureSchedule schedule = lectureScheduleService.getLectureSchedule(lectureScheduleId);

            if (lectureEnrollService.countByUserId(lectureScheduleId, userId) > 0) {
                throw new IllegalStateException("이미 해당 유저로 신청된 특강: lectureScheduleId = " + lectureScheduleId + ", userId = " + userId);
            }

            LectureEnroll enroll = LectureEnroll.of(userId, schedule.getId());

            userService.getOrCreateUser(userId);

            schedule.enroll();
            schedule = lectureScheduleService.save(schedule);
            enroll = lectureEnrollService.save(enroll);
            final Lecture lecture = lectureService.getLecture(schedule.getLectureId());
            return LectureEnrollDTO.of(enroll, schedule, lecture);
        }));
    }

    public Page<LectureEnrollDTO> getEnrolled(
            final Pageable pageable,
            final Long userId
    ) {
        userService.getUser(userId);  // 유저 없을 경우 BadCredentialsException 발생 -> 401 error

        return functionalTransaction.perform(new TransactionItem<>(true, TransactionDefinition.ISOLATION_READ_COMMITTED,
                handler -> {

            Page<LectureEnroll> enrolls = lectureEnrollService.getLectureEnrollAllByUserId(userId, pageable);

            List<Long> scheduleIds = enrolls.getContent().stream().map(LectureEnroll::getLectureScheduleId).toList();
            List<LectureSchedule> schedules = lectureScheduleService.getLectureSchedules(scheduleIds);
            Map<Long, LectureSchedule> scheduleMap = lectureScheduleService.toMap(schedules);

            List<Long> lectureIds = schedules.stream().map(LectureSchedule::getLectureId).toList();
            List<Lecture> lectures = lectureService.getLectureAllIn(lectureIds);
            Map<Long, Lecture> lectureMap = lectureService.toMap(lectures);

            List<LectureEnrollDTO> enrollDTOs = enrolls.getContent().stream().map(enroll -> {
                LectureSchedule schedule = scheduleMap.get(enroll.getLectureScheduleId());
                Lecture lecture = lectureMap.get(schedule.getLectureId());

                return LectureEnrollDTO.of(enroll, schedule, lecture);
            }).toList();

            return new PageImpl<>(enrollDTOs, pageable, enrolls.getTotalElements());
        }));
    }
}
