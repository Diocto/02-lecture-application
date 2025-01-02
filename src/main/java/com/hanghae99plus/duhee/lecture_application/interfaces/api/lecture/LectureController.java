package com.hanghae99plus.duhee.lecture_application.interfaces.api.lecture;

import com.hanghae99plus.duhee.lecture_application.application.lecture.lecture.LectureFacade;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.LectureEntity;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.UserLectureEnrollmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LectureController {
    private final LectureFacade lectureFacade;

    @PostMapping("/lectures/{id}/enrollments")
    public ResponseEntity<UserLectureEnrollmentEntity> enrollLecture(@PathVariable Long id, @RequestBody Long userId) {
        UserLectureEnrollmentEntity enrollmentEntity = this.lectureFacade.enrollUserToLecture(userId, id);
        return ResponseEntity.ok(enrollmentEntity);
    }

    @GetMapping("/users/{userId}/enrollments")
    public ResponseEntity<?> findEnrollmentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(this.lectureFacade.findEnrollmentsByUserId(userId));
    }

    @GetMapping("/lectures")
    public ResponseEntity<List<LectureEntity>> getEnrollableLectures(@RequestParam LocalDate targetDate) {
        return ResponseEntity.ok(this.lectureFacade.getEnrollableLectures(targetDate));
    }
}
