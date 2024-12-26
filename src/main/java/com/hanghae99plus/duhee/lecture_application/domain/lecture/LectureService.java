package com.hanghae99plus.duhee.lecture_application.domain.lecture;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final UserLectureEnrollmentRepository userLectureEnrollmentRepository;

    public Optional<LectureEntity> findLectureById(Long id) {
        return this.lectureRepository.findById(id);
    }

    public List<LectureEntity> getEnrollableLectures(LocalDate targetDate) {
        return this.lectureRepository.findLecturesAfter(targetDate);
    }

    public UserLectureEnrollmentEntity enrollUserToLecture(Long userId, Long lectureId) {
        LectureEntity lectureEntity = this.lectureRepository.findById(lectureId).orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));
        if (!lectureEntity.isEnrollable())
            throw new IllegalArgumentException("해당 강의는 수강신청이 불가능 합니다.");

        UserLectureEnrollmentEntity alreadyEnrolledLecture
                = this.userLectureEnrollmentRepository.findByUserIdAndLectureId(userId, lectureId);
        if (alreadyEnrolledLecture != null)
            throw new IllegalArgumentException("이미 수강신청한 강의입니다.");

        lectureEntity.setCurrentEnrollment(lectureEntity.getCurrentEnrollment() + 1);
        this.lectureRepository.save(lectureEntity);
        return this.userLectureEnrollmentRepository.save(UserLectureEnrollmentEntity.builder().userId(userId).lectureId(lectureId).build());
    }

    public List<LectureEntity> findEnrollmentsByUserId(Long userId) {
        /// 강의 정보(이름, 강사) 도 담고 있어야함.
        List<UserLectureEnrollmentEntity> enrollmentEntities = this.userLectureEnrollmentRepository.findByUserId(userId);
        return enrollmentEntities.stream()
                .map(e -> this.lectureRepository.findById(e.getLecture().getId()).orElseThrow(()->new IllegalArgumentException("해당 강의를 찾을 수 없습니다.")))
                .toList();
    }
}
