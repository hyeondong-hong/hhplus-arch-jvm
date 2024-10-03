package io.hhplus.arch.application.lecture.service;

import io.hhplus.arch.domain.lecture.entity.LectureEnroll;
import io.hhplus.arch.domain.lecture.repository.LectureEnrollRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class LectureEnrollServiceImpl implements LectureEnrollService {

    private final LectureEnrollRepository lectureEnrollRepository;

    public LectureEnrollServiceImpl(
            LectureEnrollRepository lectureEnrollRepository
    ) {
        this.lectureEnrollRepository = lectureEnrollRepository;
    }

    @Override
    public Page<LectureEnroll> getLectureEnrollAllByUserId(
            @NonNull Long userId,
            @NonNull Pageable pageable
    ) {
        return lectureEnrollRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Long countByUserId(
            @NonNull Long lectureScheduleId,
            @NonNull Long studentId
    ) {
        return lectureEnrollRepository.countByLectureScheduleIdAndUserId(lectureScheduleId, studentId);
    }

    @Override
    public void delete(@NonNull LectureEnroll lectureEnroll) {
        lectureEnrollRepository.delete(lectureEnroll);
    }

    @Override
    public LectureEnroll save(@NonNull LectureEnroll lectureEnroll) {
        return lectureEnrollRepository.save(lectureEnroll);
    }
}
