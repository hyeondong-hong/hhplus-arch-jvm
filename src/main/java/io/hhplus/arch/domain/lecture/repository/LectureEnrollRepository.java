package io.hhplus.arch.domain.lecture.repository;

import io.hhplus.arch.domain.lecture.entity.LectureEnroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureEnrollRepository {
    Page<LectureEnroll> findAllByUserId(Long userId, Pageable pageable);
    Long countByLectureScheduleIdAndUserId(Long lectureScheduleId, Long userId);
    void delete(LectureEnroll entity);
    LectureEnroll save(LectureEnroll entity);
    void deleteAll();
}
