package io.hhplus.arch.application.lecture.service;

import io.hhplus.arch.domain.lecture.entity.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface LectureService {
    Lecture getLecture(Long id);
    List<Lecture> getLectureAllIn(Collection<Long> ids);
    Map<Long, Lecture> toMap(List<Lecture> lectures);
    Page<Lecture> getLectureAll(Pageable pageable);
    Page<Lecture> getLectureAllIn(Collection<Long> ids, Pageable pageable);
    Lecture save(Lecture lecture);
}
