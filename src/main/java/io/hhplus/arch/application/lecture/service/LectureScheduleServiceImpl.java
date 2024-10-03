package io.hhplus.arch.application.lecture.service;

import io.hhplus.arch.domain.lecture.entity.LectureSchedule;
import io.hhplus.arch.domain.lecture.repository.LectureScheduleRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class LectureScheduleServiceImpl implements LectureScheduleService {

    private final LectureScheduleRepository lectureScheduleRepository;

    public LectureScheduleServiceImpl(LectureScheduleRepository lectureScheduleRepository) {
        this.lectureScheduleRepository = lectureScheduleRepository;
    }

    @Override
    public LectureSchedule save(@NonNull LectureSchedule lectureSchedule) {
        return lectureScheduleRepository.save(lectureSchedule);
    }

    @Override
    public List<Long> getAvailableLectureIds(
            @NonNull LocalDate startDate,
            @NonNull LocalDate endDate
    ) {
        return lectureScheduleRepository.findDistinctLectureIdsByLectureDateBetween(startDate, endDate);
    }

    @Override
    public LectureSchedule getLectureSchedule(@NonNull Long id) {
        return lectureScheduleRepository.getById(id);
    }

    @Override
    public List<LectureSchedule> getLectureSchedules(@NonNull Collection<Long> ids) {
        return lectureScheduleRepository.findAllByIdIn(ids);
    }

    @Override
    public Map<Long, LectureSchedule> toMap(@NonNull List<LectureSchedule> lectureSchedules) {
        return lectureSchedules.stream()
                .collect(Collectors.toMap(LectureSchedule::getId, schedule -> schedule));
    }

    @Override
    public Map<Long, List<LectureSchedule>> getMapAvailableByLectureIdIn(@NonNull Collection<Long> lectureIds) {
        return lectureScheduleRepository.findAvailableAllLectureIdMap(lectureIds);
    }
}
