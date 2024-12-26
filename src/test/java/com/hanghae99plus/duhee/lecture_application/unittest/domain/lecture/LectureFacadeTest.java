package com.hanghae99plus.duhee.lecture_application.unittest.domain.lecture;

import com.hanghae99plus.duhee.lecture_application.application.lecture.lecture.LectureFacade;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.LectureEntity;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.LectureService;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.UserLectureEnrollmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LectureFacadeTest {
    @Mock
    private LectureService lectureServiceMock;

    @InjectMocks
    private LectureFacade uut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 초기화
        this.uut = new LectureFacade(lectureServiceMock);
    }

    @Test
    public void 신청가능_강의가져오기() {
        // given
        LocalDate targetDate = LocalDate.now();
        List<LectureEntity> expected = List.of(
                mock(LectureEntity.class),
                mock(LectureEntity.class)
        );
        when(lectureServiceMock.getEnrollableLectures(targetDate)).thenReturn(expected);

        // when
        List<LectureEntity> actual = uut.getEnrollableLectures(targetDate);

        // then
        assertEquals(expected, actual);
        then(lectureServiceMock).should().getEnrollableLectures(targetDate);
    }

    @Test
    public void 강의신청() {
        // given
        Long userId = 1L;
        Long lectureId = 1L;
        UserLectureEnrollmentEntity lectureEntity = mock(UserLectureEnrollmentEntity.class);
        when(lectureServiceMock.enrollUserToLecture(userId, lectureId)).thenReturn(lectureEntity);

        // when
        UserLectureEnrollmentEntity actual = uut.enrollUserToLecture(userId, lectureId);

        // then
        assertEquals(lectureEntity, actual);
        then(lectureServiceMock).should().enrollUserToLecture(userId, lectureId);
    }

    @Test
    public void 사용자별_강의조회() {
        // given
        Long userId = 1L;
        List<LectureEntity> expected = List.of(
                mock(LectureEntity.class),
                mock(LectureEntity.class)
        );
        when(lectureServiceMock.findEnrollmentsByUserId(userId)).thenReturn(expected);

        // when
        List<LectureEntity> actual = uut.findEnrollmentsByUserId(userId);

        // then
        assertEquals(expected, actual);
        then(lectureServiceMock).should().findEnrollmentsByUserId(userId);
    }
}
