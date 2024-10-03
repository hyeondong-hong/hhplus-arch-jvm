package io.hhplus.arch.unit.application.lecture.service;

import io.hhplus.arch.application.lecture.service.LectureScheduleServiceImpl;
import io.hhplus.arch.domain.lecture.entity.LectureSchedule;
import io.hhplus.arch.domain.lecture.repository.LectureScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LectureScheduleServiceUnitTest {

    @Mock
    private LectureScheduleRepository lectureScheduleRepository;

    @InjectMocks
    private LectureScheduleServiceImpl lectureScheduleService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("강의 스케줄 목록을 맵으로 전환")
    public void convert_listToMap() {
        long lectureId = 1L;
        int capacity = 30;
        List<LectureSchedule> lectureSchedules = new ArrayList<>();
        Map<Long, LectureSchedule> compareMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            LectureSchedule lectureSchedule = new LectureSchedule();
            lectureSchedule.setId(i + 1L);
            lectureSchedule.setLectureId(lectureId);
            lectureSchedule.setCapacity(capacity);
            lectureSchedule.setLectureDate(LocalDate.now().plusDays(i));
            lectureSchedules.add(lectureSchedule);
            compareMap.put(lectureSchedule.getId(), lectureSchedule);
        }

        Map<Long, LectureSchedule> map = lectureScheduleService.toMap(lectureSchedules);
        assertEquals(compareMap, map);
    }
}
