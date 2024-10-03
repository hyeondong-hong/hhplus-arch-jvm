package io.hhplus.arch.integration.application.lecture.service;

import io.hhplus.arch.application.lecture.service.LectureEnrollService;
import io.hhplus.arch.domain.lecture.entity.LectureEnroll;
import io.hhplus.arch.domain.lecture.repository.LectureEnrollRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LectureEnrollServiceIntegrationTest {

    @Autowired
    private LectureEnrollService lectureEnrollService;

    @Autowired
    private LectureEnrollRepository lectureEnrollRepository;

    private Long userId;

    @BeforeEach
    public void setUp() {
        lectureEnrollRepository.deleteAll();

        userId = 1L;
        List<LectureEnroll> lectureEnrolls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LectureEnroll lectureEnroll = new LectureEnroll();
            lectureEnroll.setId(i + 1L);
            lectureEnroll.setUserId(userId);
            lectureEnroll.setLectureScheduleId(i + 11L);
            lectureEnrolls.add(lectureEnroll);
        }

        lectureEnrollRepository.saveAll(lectureEnrolls);
    }

    @Test
    @DisplayName("유저가 강의를 등록했는지 확인한다")
    public void serving_existsEnroll() {
        Long cnt1 = lectureEnrollService.countByUserId(11L, userId);
        Long cnt2 = lectureEnrollService.countByUserId(100L, userId);
        assertEquals(1L, cnt1, "setUp에서 등록한 scheduleId는 11~20이므로 11로 조회 시 존재해야 한다.");
        assertEquals(0L, cnt2, "setUp에서 등록한 scheduleId는 11~20이므로 100으로 조회 시 없어야 한다.");
    }
}
