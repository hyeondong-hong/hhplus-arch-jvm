package io.hhplus.arch.domain.lecture.repository;

import io.hhplus.arch.domain.lecture.entity.LectureSchedule;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface LectureScheduleRepository {
    LectureSchedule save(LectureSchedule lectureSchedule);
    LectureSchedule getById(Long id);
    List<Long> findDistinctLectureIdsByLectureDateBetween(LocalDate startDate, LocalDate endDate);
    List<LectureSchedule> findAllByIdIn(Collection<Long> ids);
    Map<Long, List<LectureSchedule>> findAvailableAllLectureIdMap(Collection<Long> lectureIds);
}
