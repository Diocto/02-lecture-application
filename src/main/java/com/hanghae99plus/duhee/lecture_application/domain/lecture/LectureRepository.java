package com.hanghae99plus.duhee.lecture_application.domain.lecture;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<LectureEntity, Long> {
    Optional<LectureEntity> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM LectureEntity l WHERE l.id = :id")
    Optional<LectureEntity> findByIdWithLock(Long id);

    @Query("SELECT l FROM LectureEntity l WHERE l.lectureDate >= :targetDate")
    List<LectureEntity> findLecturesAfter(LocalDate targetDate);
}
