package io.hhplus.arch.infrastructure.lecture.repository;

import io.hhplus.arch.domain.lecture.entity.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {
//    Page<Lecture> findAll(Pageable pageable);
    List<Lecture> findAllByIdIn(Collection<Long> ids);
    Page<Lecture> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}
