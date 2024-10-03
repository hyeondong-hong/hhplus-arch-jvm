package io.hhplus.arch.application.lecture.service;

import io.hhplus.arch.domain.lecture.entity.Lecture;
import io.hhplus.arch.domain.lecture.repository.LectureRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    public LectureServiceImpl(
            LectureRepository lectureRepository
    ) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public Lecture getLecture(@NonNull Long id) {
        return lectureRepository.getById(id);
    }

    @Override
    public Page<Lecture> getLectureAll(@NonNull Pageable pageable) {
        return lectureRepository.findAll(pageable);
    }

    @Override
    public Page<Lecture> getLectureAllIn(
            @NonNull Collection<Long> ids,
            @NonNull Pageable pageable
    ) {
        return lectureRepository.findAllByIdIn(ids, pageable);
    }

    @Override
    public List<Lecture> getLectureAllIn(@NonNull Collection<Long> ids) {
        return lectureRepository.findAllByIdIn(ids);
    }

    @Override
    public Map<Long, Lecture> toMap(@NonNull List<Lecture> lectures) {
        return lectures.stream()
                .collect(Collectors.toMap(Lecture::getId, lecture -> lecture));
    }

    @Override
    public Lecture save(@NonNull Lecture lecture) {
        return lectureRepository.save(lecture);
    }
}
