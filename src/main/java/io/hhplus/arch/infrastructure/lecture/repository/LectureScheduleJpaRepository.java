package io.hhplus.arch.infrastructure.lecture.repository;

import io.hhplus.arch.domain.lecture.entity.LectureSchedule;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureScheduleJpaRepository extends JpaRepository<LectureSchedule, Long> {
    @Query("SELECT DISTINCT l.id FROM LectureSchedule l WHERE l.lectureDate BETWEEN :startDate AND :endDate")
    List<Long> findDistinctLectureIdsByLectureDateBetween(LocalDate startDate, LocalDate endDate);
    List<LectureSchedule> findByLectureIdIn(Collection<Long> lectureIds);
    List<LectureSchedule> findByLectureIdInAndCapacityGreaterThanOrderByLectureDate(Collection<Long> lectureIds, Integer capacity);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<LectureSchedule> findById(Long id);
    List<LectureSchedule> findAllByIdIn(Collection<Long> id);
}
