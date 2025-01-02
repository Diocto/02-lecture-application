package com.hanghae99plus.duhee.lecture_application.integrationtest.interfaces.api.lecture;

import com.hanghae99plus.duhee.lecture_application.domain.lecture.LectureEntity;
import com.hanghae99plus.duhee.lecture_application.LectureApplication;
import com.hanghae99plus.duhee.lecture_application.domain.lecture.LectureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LectureApplication.class)
public class LectureConcurrentTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private LectureRepository lectureRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        lectureRepository.deleteAll();

        LectureEntity lectureEntity = LectureEntity.builder()
                .name("강의1")
                .teacherName("선생님1")
                .description("설명1")
                .lectureDate(LocalDate.now().plusDays(1))
                .maxEnrollment(30)
                .currentEnrollment(0)
                .build();
        lectureRepository.save(lectureEntity);
    }

    @Test
    public void 동시에_40명의_수강자가_수강신청을_할때_30명이_성공하고_10명이_실패해야한다() throws InterruptedException {
        int numberOfThreads = 40;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 1; i <= numberOfThreads; i++) {
            int user_id = i;
            executorService.execute(() -> {
                try {
                    mockMvc.perform(post("/lectures/1/enrollments")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(String.valueOf(user_id)))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        LectureEntity lectureEntity = lectureRepository.findById(1L).orElseThrow();
        assertEquals(30, lectureEntity.getCurrentEnrollment());
    }
}