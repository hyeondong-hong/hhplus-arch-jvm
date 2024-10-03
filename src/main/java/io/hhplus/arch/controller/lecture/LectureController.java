package io.hhplus.arch.controller.lecture;

import io.hhplus.arch.application.lecture.dto.LectureDTO;
import io.hhplus.arch.application.lecture.dto.LectureEnrollDTO;
import io.hhplus.arch.application.lecture.facade.LectureFacade;
import io.hhplus.arch.application.lecture.request.LectureEnrollRequest;
import io.hhplus.arch.common.application.dto.DateRangeDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lecture")
public class LectureController {

    private final LectureFacade lectureFacade;

    public LectureController(
            LectureFacade lectureFacade
    ) {
        this.lectureFacade = lectureFacade;
    }

    @GetMapping("/schedules")
    public ResponseEntity<Page<LectureDTO>> availableLectures(
            @PageableDefault(size = 15, sort = "id") Pageable pageable,
            @ModelAttribute DateRangeDTO dateRange
    ) {
        return new ResponseEntity<>(
                lectureFacade.getLectureAvailableAll(
                        pageable,
                        dateRange.startDate(),
                        dateRange.endDate()
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/schedule/enroll")
    public ResponseEntity<LectureEnrollDTO> enrollLecture(
            @Valid @RequestBody LectureEnrollRequest request
    ) {
        return new ResponseEntity<>(
                lectureFacade.enroll(
                        request.lectureScheduleId(),
                        request.userId()
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/enrolled")
    public ResponseEntity<Page<LectureEnrollDTO>> enrolledLectures(
            @PageableDefault(size = 15, sort = "id") Pageable pageable,
            @RequestParam @Min(1) Long userId
    ) {
        return new ResponseEntity<>(
                lectureFacade.getEnrolled(
                        pageable,
                        userId
                ),
                HttpStatus.OK
        );
    }
}
