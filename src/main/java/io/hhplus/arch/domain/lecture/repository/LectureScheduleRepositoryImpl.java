package io.hhplus.arch.domain.lecture.repository;

import io.hhplus.arch.common.application.validator.DateRangeValidator;
import io.hhplus.arch.domain.lecture.entity.LectureSchedule;
import io.hhplus.arch.infrastructure.lecture.repository.LectureScheduleJpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class LectureScheduleRepositoryImpl implements LectureScheduleRepository {

    private final LectureScheduleJpaRepository jpaRepository;
    private final DateRangeValidator dateRangeValidator;

    public LectureScheduleRepositoryImpl(
            LectureScheduleJpaRepository jpaRepository,
            DateRangeValidator dateRangeValidator
    ) {
        this.jpaRepository = jpaRepository;
        this.dateRangeValidator = dateRangeValidator;
    }

    @Override
    public LectureSchedule save(
            @NonNull LectureSchedule lectureSchedule
    ) {
        return jpaRepository.save(lectureSchedule);
    }

    @Override
    public LectureSchedule getById(
            @NonNull Long id
    ) {
        return jpaRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Long> findDistinctLectureIdsByLectureDateBetween(
            @NonNull LocalDate startDate,
            @NonNull LocalDate endDate
    ) {
        dateRangeValidator.validate(startDate, endDate);
        return jpaRepository.findDistinctLectureIdsByLectureDateBetween(startDate, endDate);
    }

    @Override
    public List<LectureSchedule> findAllByIdIn(
            @NonNull Collection<Long> ids
    ) {
        return jpaRepository.findAllByIdIn(ids);
    }

    @Override
    public Map<Long, List<LectureSchedule>> findAvailableAllLectureIdMap(
            @NonNull Collection<Long> lectureIds
    ) {
        return toMap(jpaRepository.findByLectureIdInAndCapacityGreaterThanOrderByLectureDate(lectureIds, 0));
    }

    @Override
    public List<LectureSchedule> saveAll(Iterable<LectureSchedule> lectureSchedules) {
        return jpaRepository.saveAll(lectureSchedules);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    private Map<Long, List<LectureSchedule>> toMap(
            final Collection<LectureSchedule> schedules
    ) {
        Map<Long, List<LectureSchedule>> result = new HashMap<>();
        schedules.forEach(
                entity -> result.computeIfAbsent(entity.getLectureId(), k -> new ArrayList<>()).add(entity));
        return result;
    }
}
