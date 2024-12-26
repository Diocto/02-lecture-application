package com.hanghae99plus.duhee.lecture_application.application.lecture.lecture;

import com.hanghae99plus.duhee.lecture_application.domain.lecture.LectureEntity;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.LectureService;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.UserLectureEnrollmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LectureFacade {
    private final LectureService lectureService;

    public List<LectureEntity> getEnrollableLectures(LocalDate targetDate) {
        return this.lectureService.getEnrollableLectures(targetDate);
    }

    public UserLectureEnrollmentEntity enrollUserToLecture(Long userId, Long lectureId) {
        UserLectureEnrollmentEntity lectureEntity = this.lectureService.enrollUserToLecture(userId, lectureId);
        return lectureEntity;
    }

    public List<LectureEntity> findEnrollmentsByUserId(Long userId) {
        return this.lectureService.findEnrollmentsByUserId(userId);
    }
}
