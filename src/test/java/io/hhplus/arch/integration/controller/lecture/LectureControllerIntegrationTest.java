package io.hhplus.arch.integration.controller.lecture;

import io.hhplus.arch.application.lecture.dto.LectureEnrollDTO;
import io.hhplus.arch.application.lecture.request.LectureEnrollRequest;
import io.hhplus.arch.domain.lecture.entity.Lecture;
import io.hhplus.arch.domain.lecture.entity.LectureSchedule;
import io.hhplus.arch.domain.lecture.repository.LectureRepository;
import io.hhplus.arch.domain.lecture.repository.LectureScheduleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LectureControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureScheduleRepository lectureScheduleRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeEach
    public void setUp() {
        List<String> lecturerNames = List.of(
                "렌", "타일러", "이석범", "하헌우", "허재", "김종협", "로이", "토투");

        List<Lecture> lectures = new ArrayList<>();
        List<LectureSchedule> lectureSchedules = new ArrayList<>();
        long idx = 1L;
        long idxSchedule = 1L;
        for (String name : lecturerNames) {
            for (int i = 0; i < 1; i++) {
                Lecture lecture = new Lecture();
                lecture.setId(idx++);
                lecture.setTitle(name + "의 특강");
                lecture.setLecturerName(name);
//                lecture.setDefaultCapacity(30);
                lectures.add(lecture);

                for (int j = 0; j < 1; j++) {
                    LectureSchedule lectureSchedule = new LectureSchedule();
                    lectureSchedule.setId(idxSchedule++);
                    lectureSchedule.setLectureId(lecture.getId());
                    lectureSchedule.setCapacity(lecture.getDefaultCapacity());
                    lectureSchedule.setLectureDate(LocalDate.now().plusDays(j));
                    lectureSchedules.add(lectureSchedule);
                }
            }
        }
        lectureRepository.saveAll(lectures);
        lectureScheduleRepository.saveAll(lectureSchedules);
    }

    @AfterEach
    public void tearDown() {
        lectureRepository.deleteAll();
        lectureScheduleRepository.deleteAll();
    }

    @Test
    @DisplayName("유저가 특강을 신청한다")
    public void api_enroll() {
        String url = getBaseUrl() + "/lecture/schedule/enroll";

        LectureEnrollRequest request = new LectureEnrollRequest(1L, 101L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LectureEnrollRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<LectureEnrollDTO> response = restTemplate.postForEntity(url, httpEntity, LectureEnrollDTO.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(Objects.requireNonNull(response.getBody()).id());
    }

    @Test
    @DisplayName("40명의 유저가 하나의 특강에 동시에 신청하고 30명만 성공한다")
    public void api_enrollConcurrently() throws InterruptedException {
        Long lectureScheduleId = 2L;
        List<Long> userIds = IntStream.range(0, 40).mapToObj(i -> i + 1L).toList();

        String url = getBaseUrl() + "/lecture/schedule/enroll";

        CountDownLatch standbyLatch = new CountDownLatch(1);
        CountDownLatch latch = new CountDownLatch(userIds.size());

        final Map<Long, ResponseEntity<LectureEnrollDTO>> map = new HashMap<>();
        userIds.forEach(userId -> {

            CompletableFuture.runAsync(() -> {
                try {
                    standbyLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                LectureEnrollRequest request = new LectureEnrollRequest(lectureScheduleId, userId);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<LectureEnrollRequest> httpEntity = new HttpEntity<>(request, headers);

                ResponseEntity<LectureEnrollDTO> response = restTemplate.postForEntity(url, httpEntity, LectureEnrollDTO.class);
                map.put(userId, response);
                latch.countDown();
            });
        });
        standbyLatch.countDown();
        latch.await();

        int status2xx = 0;
        int status4xx = 0;
        int status5xx = 0;
        for (Map.Entry<Long, ResponseEntity<LectureEnrollDTO>> entry : map.entrySet()) {
            Long userId = entry.getKey();
            ResponseEntity<LectureEnrollDTO> response = entry.getValue();
            if (response.getStatusCode().is2xxSuccessful()) {
                status2xx++;
            } else if (response.getStatusCode().is4xxClientError()) {
                status4xx++;
            } else if (response.getStatusCode().is5xxServerError()) {
                status5xx++;
            }
        }
        assertThat(status2xx).isEqualTo(30);
        assertThat(status4xx).isEqualTo(10);
        assertThat(status5xx).isEqualTo(0);
    }

    @Test
    @DisplayName("한 명의 유저가 하나의 특강에 5번 신청하고 한 번만 성공한다")
    public void api_enrollConflict() throws InterruptedException {
        Long lectureScheduleId = 3L;
        Long userId = 201L;
        int count = 5;

        String url = getBaseUrl() + "/lecture/schedule/enroll";

        CountDownLatch standbyLatch = new CountDownLatch(1);
        CountDownLatch latch = new CountDownLatch(count);

        final List<ResponseEntity<LectureEnrollDTO>> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            CompletableFuture.runAsync(() -> {
                try {
                    standbyLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                LectureEnrollRequest request = new LectureEnrollRequest(lectureScheduleId, userId);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<LectureEnrollRequest> httpEntity = new HttpEntity<>(request, headers);

                ResponseEntity<LectureEnrollDTO> response = restTemplate.postForEntity(url, httpEntity, LectureEnrollDTO.class);
                list.add(response);
                latch.countDown();
            });
        }
        standbyLatch.countDown();
        latch.await();

        int status2xx = 0;
        int status4xx = 0;
        int status5xx = 0;
        for (ResponseEntity<LectureEnrollDTO> response : list) {
            if (response.getStatusCode().is2xxSuccessful()) {
                status2xx++;
            } else if (response.getStatusCode().is4xxClientError()) {
                status4xx++;
            } else if (response.getStatusCode().is5xxServerError()) {
                status5xx++;
            }
        }
        assertThat(status2xx).isEqualTo(1);
        assertThat(status4xx).isEqualTo(4);
        assertThat(status5xx).isEqualTo(0);
    }
}
