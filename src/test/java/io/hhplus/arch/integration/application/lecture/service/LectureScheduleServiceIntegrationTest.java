package io.hhplus.arch.integration.application.lecture.service;

import io.hhplus.arch.application.lecture.service.LectureScheduleService;
import io.hhplus.arch.domain.lecture.entity.LectureSchedule;
import io.hhplus.arch.domain.lecture.repository.LectureScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LectureScheduleServiceIntegrationTest {

    @Autowired
    private LectureScheduleService lectureScheduleService;

    @Autowired
    private LectureScheduleRepository lectureScheduleRepository;

    private Long lectureId;

    @BeforeEach
    public void setUp() {
        lectureScheduleRepository.deleteAll();

        lectureId = 1L;
        int capacity = 30;
        List<LectureSchedule> lectureSchedules = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LectureSchedule lectureSchedule = new LectureSchedule();
            lectureSchedule.setId(i + 1L);
            lectureSchedule.setLectureId(lectureId);
            lectureSchedule.setCapacity(capacity);
            lectureSchedule.setLectureDate(LocalDate.now().plusDays(i));
            lectureSchedules.add(lectureSchedule);
        }

        lectureScheduleRepository.saveAll(lectureSchedules);
    }

    @Test
    public void serving_availableLectureIdsByBetween() {
        List<Long> lectureIds = lectureScheduleService.getAvailableLectureIds(LocalDate.now(), LocalDate.now());
        assertEquals(1, lectureIds.size());
        assertEquals(1L, lectureIds.get(0));
    }

    @Test
    public void serving_lectureSchedule() {
        Long id = 4L;
        LectureSchedule lectureSchedule = lectureScheduleService.getLectureSchedule(id);
        assertEquals(lectureSchedule.getId(), id);
        assertEquals(LocalDate.now().plusDays(id - 1), lectureSchedule.getLectureDate());
    }

    @Test
    public void serving_lectureScheduleMap() {
        Map<Long, List<LectureSchedule>> map = lectureScheduleService.getMapAvailableByLectureIdIn(
                List.of(lectureId));
        assertEquals(1, map.size());
        assertEquals(10, map.get(lectureId).size());
    }
}
