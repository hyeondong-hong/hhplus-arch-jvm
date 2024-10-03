package io.hhplus.arch.application.lecture.service;

import io.hhplus.arch.domain.lecture.entity.LectureEnroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureEnrollService {
    Page<LectureEnroll> getLectureEnrollAllByUserId(Long userId, Pageable pageable);
    Long countByUserId(Long lectureScheduleId, Long userId);
    void delete(LectureEnroll lectureEnroll);
    LectureEnroll save(LectureEnroll lectureEnroll);
}
