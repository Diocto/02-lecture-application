package com.hanghae99plus.duhee.lecture_application.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLectureEnrollmentRepository extends JpaRepository<UserLectureEnrollmentEntity, Long> {
    Optional<UserLectureEnrollmentEntity> findById(Long id);
    UserLectureEnrollmentEntity save(UserLectureEnrollmentEntity userLectureEnrollmentEntity);
    List<UserLectureEnrollmentEntity> findByUserId(Long userId);
    UserLectureEnrollmentEntity findByUserIdAndLectureId(Long userId, Long lectureId);
}
