package io.hhplus.arch.unit.domain.lecture.entity;

import io.hhplus.arch.domain.lecture.entity.LectureSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LectureScheduleUnitTest {

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("자리 수 차감 및 신청 가능 상태 반환이 정상적으로 이뤄지는지 확인")
    public void failure_scheduleIsFull() {
        LectureSchedule lectureSchedule = new LectureSchedule();
        lectureSchedule.setCapacity(30);

        for (int i = 0; i < 30; i++) {
            assertTrue(lectureSchedule.enroll());
        }
        assertFalse(lectureSchedule.enroll());
    }
}
