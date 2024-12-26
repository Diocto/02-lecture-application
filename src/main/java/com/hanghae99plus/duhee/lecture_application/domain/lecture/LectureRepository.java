package com.hanghae99plus.duhee.lecture_application.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<LectureEntity, Long> {
    Optional<LectureEntity> findById(Long id);

    @Query("SELECT l FROM LectureEntity l WHERE l.lectureDate >= :targetDate")
    List<LectureEntity> findLecturesAfter(LocalDate targetDate);
}
