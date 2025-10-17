package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleRequestDTOTest {

    @Test
    void testConstructorWithParameters_Valid() {
        ScheduleRequestDTO request = new ScheduleRequestDTO("REQ001", "STU123", "GRP001", "GRP002", "Conflict with another class");

        assertEquals("REQ001", request.getRequestId());
        assertEquals("STU123", request.getStudentId());
        assertEquals("GRP001", request.getCurrentGroupId());
        assertEquals("GRP002", request.getRequestedGroupId());
        assertEquals("Conflict with another class", request.getReason());
        assertEquals("PENDING", request.getStatus());
        assertNotNull(request.getSubmissionDate());
    }

    @Test
    void testConstructorWithParameters_NullValues() {
        ScheduleRequestDTO request = new ScheduleRequestDTO(null, null, null, null, null);

        assertNull(request.getRequestId());
        assertNull(request.getStudentId());
        assertNull(request.getCurrentGroupId());
        assertNull(request.getRequestedGroupId());
        assertNull(request.getReason());
        assertEquals("PENDING", request.getStatus());
        assertNotNull(request.getSubmissionDate());
    }

    @Test
    void testAddReviewStep_FirstReview() {
        ScheduleRequestDTO request = new ScheduleRequestDTO();
        ScheduleRequestDTO.ReviewStepDetailDTO review = new ScheduleRequestDTO.ReviewStepDetailDTO("USER1", "COORDINATOR", "APPROVED", "Looks good");

        request.addReviewStep(review);

        assertEquals(1, request.getReviewHistory().size());
        assertEquals("USER1", request.getReviewHistory().get(0).getUserId());
        assertEquals("COORDINATOR", request.getReviewHistory().get(0).getUserRole());
        assertEquals("APPROVED", request.getReviewHistory().get(0).getAction());
        assertEquals("Looks good", request.getReviewHistory().get(0).getComments());
        assertNotNull(request.getReviewHistory().get(0).getTimestamp());
    }

    @Test
    void testAddReviewStep_MultipleReviews() {
        ScheduleRequestDTO request = new ScheduleRequestDTO();
        ScheduleRequestDTO.ReviewStepDetailDTO review1 = new ScheduleRequestDTO.ReviewStepDetailDTO("USER1", "TEACHER", "PENDING", "Needs review");
        ScheduleRequestDTO.ReviewStepDetailDTO review2 = new ScheduleRequestDTO.ReviewStepDetailDTO("USER2", "COORDINATOR", "APPROVED", "Approved");

        request.addReviewStep(review1);
        request.addReviewStep(review2);

        assertEquals(2, request.getReviewHistory().size());
        assertEquals("TEACHER", request.getReviewHistory().get(0).getUserRole());
        assertEquals("COORDINATOR", request.getReviewHistory().get(1).getUserRole());
    }

    @Test
    void testReviewStepDetailDTOConstructor_Valid() {
        ScheduleRequestDTO.ReviewStepDetailDTO review = new ScheduleRequestDTO.ReviewStepDetailDTO("USER1", "ADMIN", "REJECTED", "Invalid reason");

        assertEquals("USER1", review.getUserId());
        assertEquals("ADMIN", review.getUserRole());
        assertEquals("REJECTED", review.getAction());
        assertEquals("Invalid reason", review.getComments());
        assertNotNull(review.getTimestamp());
    }

    @Test
    void testReviewStepDetailDTOConstructor_NullValues() {
        ScheduleRequestDTO.ReviewStepDetailDTO review = new ScheduleRequestDTO.ReviewStepDetailDTO(null, null, null, null);

        assertNull(review.getUserId());
        assertNull(review.getUserRole());
        assertNull(review.getAction());
        assertNull(review.getComments());
        assertNotNull(review.getTimestamp());
    }

    @Test
    void testSettersAndGetters_Valid() {
        ScheduleRequestDTO request = new ScheduleRequestDTO();
        Date now = new Date();
        Date later = new Date(now.getTime() + 100000);

        request.setRequestId("REQ002");
        request.setStudentId("STU456");
        request.setCurrentGroupId("GRP003");
        request.setRequestedGroupId("GRP004");
        request.setReason("Personal reasons");
        request.setStatus("APPROVED");
        request.setSubmissionDate(now);
        request.setResolutionDate(later);

        assertEquals("REQ002", request.getRequestId());
        assertEquals("STU456", request.getStudentId());
        assertEquals("GRP003", request.getCurrentGroupId());
        assertEquals("GRP004", request.getRequestedGroupId());
        assertEquals("Personal reasons", request.getReason());
        assertEquals("APPROVED", request.getStatus());
        assertEquals(now, request.getSubmissionDate());
        assertEquals(later, request.getResolutionDate());
    }

    @Test
    void testSettersAndGetters_NullValues() {
        ScheduleRequestDTO request = new ScheduleRequestDTO();

        request.setRequestId(null);
        request.setStudentId(null);
        request.setCurrentGroupId(null);
        request.setRequestedGroupId(null);
        request.setReason(null);
        request.setStatus(null);
        request.setSubmissionDate(null);
        request.setResolutionDate(null);

        assertNull(request.getRequestId());
        assertNull(request.getStudentId());
        assertNull(request.getCurrentGroupId());
        assertNull(request.getRequestedGroupId());
        assertNull(request.getReason());
        assertNull(request.getStatus());
        assertNull(request.getSubmissionDate());
        assertNull(request.getResolutionDate());
    }
}
