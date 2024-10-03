package io.hhplus.arch.unit.application.lecture.service;

import io.hhplus.arch.application.lecture.service.LectureScheduleServiceImpl;
import io.hhplus.arch.application.lecture.service.LectureServiceImpl;
import io.hhplus.arch.domain.lecture.entity.Lecture;
import io.hhplus.arch.domain.lecture.entity.LectureSchedule;
import io.hhplus.arch.domain.lecture.repository.LectureRepository;
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
public class LectureServiceUnitTest {

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    private LectureServiceImpl lectureService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("강의 스케줄 목록을 맵으로 전환")
    public void convert_listToMap() {
        long lectureId = 1L;
        List<String> lecturerNames = List.of(
                "렌", "타일러", "이석범", "하헌우", "허재", "김종협", "로이", "토투");
        List<Lecture> lectures = new ArrayList<>();
        Map<Long, Lecture> compareMap = new HashMap<>();
        for (int i = 0; i < lecturerNames.size(); i++) {
            String lecturerName = lecturerNames.get(i);
            Lecture lecture = new Lecture();
            lecture.setId(i + 1L);
            lecture.setLecturerName(lecturerName);
            lecture.setTitle(lecturerName + "의 특강");
            lectures.add(lecture);
            compareMap.put(lecture.getId(), lecture);
        }

        Map<Long, Lecture> map = lectureService.toMap(lectures);
        assertEquals(compareMap, map);
    }
}
