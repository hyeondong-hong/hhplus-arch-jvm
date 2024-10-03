package io.hhplus.arch.domain.lecture.repository;

import io.hhplus.arch.domain.lecture.entity.Lecture;
import io.hhplus.arch.infrastructure.lecture.repository.LectureJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository jpaRepository;

    public LectureRepositoryImpl(
            LectureJpaRepository jpaRepository
    ) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Lecture> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Page<Lecture> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public List<Lecture> findAllByIdIn(Collection<Long> ids) {
        return jpaRepository.findAllByIdIn(ids);
    }

    @Override
    public Page<Lecture> findAllByIdIn(Collection<Long> ids, Pageable pageable) {
        return jpaRepository.findAllByIdIn(ids, pageable);
    }

    @Override
    public Lecture getById(Long id) {
        return jpaRepository.findById(id).orElseThrow();
    }

    @Override
    public Lecture save(Lecture lecture) {
        return jpaRepository.save(lecture);
    }

    @Override
    public List<Lecture> saveAll(Iterable<Lecture> lectures) {
        return jpaRepository.saveAll(lectures);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
