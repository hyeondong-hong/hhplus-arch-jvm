package io.hhplus.arch.infrastructure.lecture.repository;

import io.hhplus.arch.domain.lecture.entity.LectureEnroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureEnrollJpaRepository extends JpaRepository<LectureEnroll, Long> {
    Page<LectureEnroll> findAllByUserId(Long userId, Pageable pageable);
    Long countByLectureScheduleIdAndUserId(Long lectureScheduleId, Long userId);
}
