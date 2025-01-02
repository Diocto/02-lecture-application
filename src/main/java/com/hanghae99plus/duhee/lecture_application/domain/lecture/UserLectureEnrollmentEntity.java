package com.hanghae99plus.duhee.lecture_application.domain.lecture;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_lecture_enrollment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLectureEnrollmentEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "lecture_id")
    private Long lectureId;

    @OneToOne
    @JoinColumn(name = "lecture_id", insertable = false, updatable = false)
    private LectureEntity lecture;
}
