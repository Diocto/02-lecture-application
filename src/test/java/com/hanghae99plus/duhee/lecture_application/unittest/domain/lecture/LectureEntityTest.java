package com.hanghae99plus.duhee.lecture_application.unittest.domain.lecture;

import com.hanghae99plus.duhee.lecture_application.domain.lecture.LectureEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LectureEntityTest {
    private LectureEntity uut;

    @Test
    public void 강의_날짜안지남_최대인원초과_신청불가() {
        // given
        uut = LectureEntity.builder()
                .name("test")
                .teacherName("test")
                .description("test")
                .lectureDate(LocalDate.now().plusDays(1))
                .maxEnrollment(1)
                .currentEnrollment(1)
                .build();
        // when
        // then
        assertEquals(uut.isEnrollable(), false);
    }

    @Test
    public void 강의_날짜지남_최대인원미초과_신청불가() {
        // given
        uut = LectureEntity.builder()
                .name("test")
                .teacherName("test")
                .description("test")
                .lectureDate(LocalDate.now())
                .maxEnrollment(1)
                .currentEnrollment(0)
                .build();
        // when
        // then
        assertEquals(uut.isEnrollable(), false);
    }

    @Test
    public void 강의_날짜지남_최대인원초과_신청불가() {
        // given
        uut = LectureEntity.builder()
                .name("test")
                .teacherName("test")
                .description("test")
                .lectureDate(LocalDate.now())
                .maxEnrollment(1)
                .currentEnrollment(1)
                .build();
        // when
        // then
        assertEquals(uut.isEnrollable(), false);
    }

    @Test
    public void 강의_날짜안지남_최대인원미초과_신청가능() {
        // given
        uut = LectureEntity.builder()
                .name("test")
                .teacherName("test")
                .description("test")
                .lectureDate(LocalDate.now().plusDays(1))
                .maxEnrollment(1)
                .currentEnrollment(0)
                .build();
        // when
        // then
        assertEquals(uut.isEnrollable(), true);
    }
}
