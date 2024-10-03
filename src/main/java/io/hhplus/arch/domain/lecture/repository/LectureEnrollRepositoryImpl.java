package io.hhplus.arch.domain.lecture.repository;

import io.hhplus.arch.domain.lecture.entity.LectureEnroll;
import io.hhplus.arch.infrastructure.lecture.repository.LectureEnrollJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class LectureEnrollRepositoryImpl implements LectureEnrollRepository {

    private final LectureEnrollJpaRepository jpaRepository;

    public LectureEnrollRepositoryImpl(
            LectureEnrollJpaRepository jpaRepository
    ) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<LectureEnroll> findAllByUserId(Long userId, Pageable pageable) {
        return jpaRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Long countByLectureScheduleIdAndUserId(
            Long lectureScheduleId,
            Long userId
    ) {
        return jpaRepository.countByLectureScheduleIdAndUserId(lectureScheduleId, userId);
    }

    @Override
    public void delete(LectureEnroll entity) {
        jpaRepository.delete(entity);
    }

    @Override
    public LectureEnroll save(LectureEnroll entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
