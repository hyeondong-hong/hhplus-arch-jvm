package io.hhplus.arch.domain.lecture.repository;

import io.hhplus.arch.domain.lecture.entity.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface LectureRepository {
    List<Lecture> findAll();
    Page<Lecture> findAll(Pageable pageable);
    List<Lecture> findAllByIdIn(Collection<Long> ids);
    Page<Lecture> findAllByIdIn(Collection<Long> ids, Pageable pageable);
    Lecture getById(Long id);
    Lecture save(Lecture entity);
    void deleteAll();
}
