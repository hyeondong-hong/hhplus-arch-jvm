package io.hhplus.arch.application.lecture.service;

import io.hhplus.arch.domain.lecture.entity.LectureSchedule;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface LectureScheduleService {
    List<Long> getAvailableLectureIds(LocalDate startDate, LocalDate endDate);
    LectureSchedule getLectureSchedule(Long id);
    List<LectureSchedule> getLectureSchedules(Collection<Long> lectureScheduleIds);
    Map<Long, LectureSchedule> toMap(List<LectureSchedule> lectureSchedules);
    LectureSchedule save(LectureSchedule lectureSchedule);
    Map<Long, List<LectureSchedule>> getMapAvailableByLectureIdIn(Collection<Long> lectureIds);
}
