package io.hhplus.arch.integration.application.lecture.service;

import io.hhplus.arch.application.lecture.service.LectureService;
import io.hhplus.arch.domain.lecture.entity.Lecture;
import io.hhplus.arch.domain.lecture.repository.LectureRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LectureServiceIntegrationTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @BeforeEach
    public void setUp() {
        lectureRepository.deleteAll();

        List<String> lecturerNames = List.of(
                "렌", "타일러", "이석범", "하헌우", "허재", "김종협", "로이", "토투");

        List<Lecture> lectures = new ArrayList<>();
        long idx = 1L;
        for (String name : lecturerNames) {
            for (int i = 0; i < 5; i++) {
                Lecture lecture = new Lecture();
                lecture.setId(idx++);
                lecture.setTitle(name + "의 특강");
                lecture.setLecturerName(name);
                lectures.add(lecture);
            }
        }
        lectureRepository.saveAll(lectures);
    }

    @Test
    @DisplayName("강의 엔티티를 가져오는데에 실패한다")
    public void serving_lectureFailure() {
        assertThrows(NoSuchElementException.class, () -> lectureService.getLecture(100L));
    }

    @Test
    @DisplayName("강의 엔티티를 가져온다")
    public void serving_lecture() {
        Long lectureId = 1L;
        Lecture lecture = lectureService.getLecture(lectureId);
        assertEquals(lectureId, lecture.getId());
    }

    @Test
    @DisplayName("강의 엔티티 목록을 가져온다")
    public void serving_lectures() {
        List<Long> lectureIds = List.of(1L, 2L, 3L, 4L, 5L);
        List<Lecture> lectures = lectureService.getLectureAllIn(lectureIds);
        assertEquals(5, lectures.size());
        assertEquals(
                lectures.stream().map(Lecture::getId).toList(),
                lectureIds
        );
    }

    @Test
    @DisplayName("강의 엔티티 전체 목록을 페이지로 가져온다")
    public void serving_lecturesPage() {
        int pageSize = 15;
        List<Long> lectureIds = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            lectureIds.add(i + 1L);
        }
        Page<Lecture> lectures = lectureService.getLectureAll(
                PageRequest.of(0, pageSize, Sort.by("id")));
        assertEquals(15, lectures.getContent().size());
        assertEquals(
                lectures.getContent().stream().map(Lecture::getId).toList(),
                lectureIds
        );
    }
}
