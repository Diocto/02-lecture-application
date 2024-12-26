package com.hanghae99plus.duhee.lecture_application.domain.lecture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LectureEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "teacher_name")
    private String teacherName;
    @Column(name = "description")
    private String description;
    @Column(name = "lecture_date")
    private LocalDate lectureDate;
    @Column(name = "max_enrollment")
    private Integer maxEnrollment = 30;
    @Column(name = "current_enrollment")
    private Integer currentEnrollment = 0;

    @JsonIgnore
    public Boolean isEnrollable() {
        Boolean isThereAvailableSeats = this.currentEnrollment < this.maxEnrollment;
        Boolean isBeforeLectureDate = LocalDate.now().isBefore(this.lectureDate);
        return isThereAvailableSeats && isBeforeLectureDate;
    }
}
