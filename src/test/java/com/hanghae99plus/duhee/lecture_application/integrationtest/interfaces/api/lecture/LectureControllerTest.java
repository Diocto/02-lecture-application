package com.hanghae99plus.duhee.lecture_application.integrationtest.interfaces.api.lecture;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hanghae99plus.duhee.lecture_application.LectureApplication;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.LectureEntity;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.LectureRepository;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.UserLectureEnrollmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LectureApplication.class)
@AutoConfigureMockMvc
public class LectureControllerTest {
    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserLectureEnrollmentRepository userLectureEnrollmentRepository;

    @Autowired
    private MockMvc mockMvc;

    /// helper
    ObjectMapper objectMapper = new ObjectMapper();

    /// given
    private List<LectureEntity> defaultLectures;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        // 기본적인 강의 목록을 DB 에 저장한다
        LectureEntity lectureEntity = LectureEntity.builder()
                .name("강의1")
                .teacherName("선생님1")
                .description("설명1")
                .lectureDate(LocalDate.parse("2021-10-10"))
                .maxEnrollment(30)
                .currentEnrollment(0)
                .build();

        LectureEntity lectureEntity2 = LectureEntity.builder()
                .name("강의2")
                .teacherName("선생님2")
                .description("설명2")
                .lectureDate(LocalDate.parse("2021-10-11"))
                .maxEnrollment(30)
                .currentEnrollment(0)
                .build();

        LectureEntity lectureEntity3 = LectureEntity.builder()
                .name("강의3")
                .teacherName("선생님3")
                .description("설명3")
                .lectureDate(LocalDate.parse("2021-10-12"))
                .maxEnrollment(30)
                .currentEnrollment(0)
                .build();

        defaultLectures = List.of(lectureEntity, lectureEntity2, lectureEntity3);
        lectureRepository.saveAll(defaultLectures);
    }

    @AfterEach
    public void tearDown() {
        userLectureEnrollmentRepository.deleteAll();
        lectureRepository.deleteAll();
    }

    @Test
    public void 신청_가능한_강의_조회_여러개() {
        // given
        /// 기본조건 : 강의가 3개 저장되어 있다.
        // when
        // then
        /// 요청 : 신청 가능한 강의 조회
        assertDoesNotThrow(()->{
            String jsonResponse = mockMvc.perform(get("/lectures").queryParam("targetDate", "2021-10-09"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            List<LectureEntity> lectures = List.copyOf(objectMapper.readValue(jsonResponse, new TypeReference<List<LectureEntity>>() {}));
            assertEquals(lectures, defaultLectures);
        });
    }

    @Test
    public void 신청_가능한_강의_조회_하나도없음() {
        assertDoesNotThrow(()->{
            String jsonResponse = mockMvc.perform(get("/lectures").queryParam("targetDate", "2021-10-30"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            assertEquals(jsonResponse, "[]");
        });
    }

    @Test
    public void 신청_가능한_강의가_일부만적용됨() {
        // given
        /// 기본조건 : 강의가 3개 저장되어 있다.
        // when
        // then
        /// 요청 : 신청 가능한 강의 조회
        assertDoesNotThrow(()->{
            String jsonResponse = mockMvc.perform(get("/lectures").queryParam("targetDate", "2021-10-11"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            List<LectureEntity> lectures = List.copyOf(objectMapper.readValue(jsonResponse, new TypeReference<List<LectureEntity>>() {}));
            assertEquals(lectures.size(), 2);
        });
    }

    @Test
    public void 강의신청_존재하지않는_강의신청() {
        // given
        /// 기본조건 : 강의가 3개 저장되어 있다.
        // when
        // then
        /// 요청 : 강의 신청. request body 에 user_id 입력
        assertDoesNotThrow(()-> {
            String jsonReponse =
                    mockMvc.perform(post("/lectures/1000/enrollments")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("1"))
                            .andExpect(status().isBadRequest())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
            assertEquals(jsonReponse, "해당 강의를 찾을 수 없습니다.");

        });
    }

    @Test
    public void 강의신청_수강인원이_가득차서_수강신청불가() {
        // given
        /// 가득찬 강의
        LectureEntity lectureEntity = LectureEntity.builder()
                .name("강의4")
                .teacherName("선생님4")
                .description("설명4")
                .lectureDate(LocalDate.parse("2021-10-11"))
                .maxEnrollment(30)
                .currentEnrollment(30)
                .build();
        // when
        lectureRepository.save(lectureEntity);

        // then
        /// 요청 : 강의 신청
        assertDoesNotThrow(()->{
            String jsonReponse =
                    mockMvc.perform(post("/lectures/" +lectureEntity.getId() + "/enrollments")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("1"))
                            .andExpect(status().isBadRequest())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
            assertEquals(jsonReponse, "해당 강의는 수강신청이 불가능 합니다.");
        });
    }

    @Test
    public void 강의신청_이미_수강신청한_강의() {
        // given
        LectureEntity lectureEntity = LectureEntity.builder()
                .name("강의4")
                .teacherName("선생님4")
                .description("설명4")
                .lectureDate(LocalDate.now().plusDays(1))
                .maxEnrollment(30)
                .currentEnrollment(0)
                .build();
        lectureRepository.save(lectureEntity);
        assertDoesNotThrow(()-> {
            mockMvc.perform(
                    post("/lectures/" + lectureEntity.getId() + "/enrollments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("1"))
                    .andExpect(status().isOk());
        });

        // when
        /// 똑같은 강의 1을 다시 수강신청
        // then
        /// 요청 : 강의 신청 실패
        assertDoesNotThrow(()->{
            String jsonReponse =
                    mockMvc.perform(post("/lectures/" + lectureEntity.getId() + "/enrollments")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("1"))
                            .andExpect(status().isBadRequest())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
            assertEquals(jsonReponse, "이미 수강신청한 강의입니다.");
        });
    }

    @Test
    public void 강의신청_성공(){
        // given
        LectureEntity lectureEntity = LectureEntity.builder()
                .name("강의4")
                .teacherName("선생님4")
                .description("설명4")
                .lectureDate(LocalDate.now().plusDays(1))
                .maxEnrollment(30)
                .currentEnrollment(0)
                .build();
        lectureRepository.save(lectureEntity);

        // when
        // then
        assertDoesNotThrow(()->{
            mockMvc.perform(post("/lectures/" + lectureEntity.getId() + "/enrollments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("1"))
                    .andExpect(status().isOk());
        });
    }

    @Test
    public void 신청강의조회_강의신청을하지않음(){
        // given
        // when
        // then
        assertDoesNotThrow(()->{
            String jsonReponse =
                    mockMvc.perform(get("/users/1/enrollments"))
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
            assertEquals(jsonReponse, "[]");
        });
    }

    @Test
    public void 신청강의조회_강의신청을함(){
        // given
        LectureEntity lectureEntity = LectureEntity.builder()
                .name("강의4")
                .teacherName("선생님4")
                .description("설명4")
                .lectureDate(LocalDate.now().plusDays(1))
                .maxEnrollment(30)
                .currentEnrollment(0)
                .build();
        lectureRepository.save(lectureEntity);
        assertDoesNotThrow(()-> {
            mockMvc.perform(
                    post("/lectures/" + lectureEntity.getId() + "/enrollments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("1"))
                    .andExpect(status().isOk());
        });

        // when
        // then
        assertDoesNotThrow(()->{
            String jsonReponse =
                    mockMvc.perform(get("/users/1/enrollments"))
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
            List<LectureEntity> lectures = List.copyOf(objectMapper.readValue(jsonReponse, new TypeReference<List<LectureEntity>>() {}));
            assertEquals(lectures.size(), 1);
            LectureEntity lectureExpect = lectureRepository.findById(lectureEntity.getId()).orElseThrow();
            assertEquals(lectures.get(0), lectureExpect);
        });
    }
}
