package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.ChangeRequestDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProcessingServiceTest {

    private ProcessingService service;
    private ChangeRequestDTO requestData;

    private Subject originalSubject;
    private Subject targetSubject;
    private Student student;

    @BeforeEach
    void setUp() throws Exception {
        service = new ProcessingService();

        student = new Student();
        var careerField = Student.class.getDeclaredField("career");
        careerField.setAccessible(true);
        careerField.set(student, "Engineering");

        originalSubject = new Subject();
        var nameField = Subject.class.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(originalSubject, "Math");
        var classTimeField = Subject.class.getDeclaredField("classTime");
        classTimeField.setAccessible(true);
        classTimeField.set(originalSubject, "08:00");

        targetSubject = new Subject();
        var nameField2 = Subject.class.getDeclaredField("name");
        nameField2.setAccessible(true);
        nameField2.set(targetSubject, "Physics");
        var classTimeField2 = Subject.class.getDeclaredField("classTime");
        classTimeField2.setAccessible(true);
        classTimeField2.set(targetSubject, "10:00");
        var quotasField = Subject.class.getDeclaredField("quotas");
        quotasField.setAccessible(true);
        quotasField.set(targetSubject, 30);
        var registeredField = Subject.class.getDeclaredField("registered");
        registeredField.setAccessible(true);
        registeredField.set(targetSubject, 20);

        requestData = new ChangeRequestDTO();
        var typeField = ChangeRequestDTO.class.getDeclaredField("requestType");
        typeField.setAccessible(true);
        typeField.set(requestData, "REQUEST_FOR_GROUP_CHANGE");

        var studentField = ChangeRequestDTO.class.getDeclaredField("student");
        studentField.setAccessible(true);
        studentField.set(requestData, student);

        var oSubjectField = ChangeRequestDTO.class.getDeclaredField("originalSubject");
        oSubjectField.setAccessible(true);
        oSubjectField.set(requestData, originalSubject);

        var tSubjectField = ChangeRequestDTO.class.getDeclaredField("targetSubject");
        tSubjectField.setAccessible(true);
        tSubjectField.set(requestData, targetSubject);

        var oGroupField = ChangeRequestDTO.class.getDeclaredField("originalGroup");
        oGroupField.setAccessible(true);
        oGroupField.set(requestData, 101);

        var tGroupField = ChangeRequestDTO.class.getDeclaredField("targetGroup");
        tGroupField.setAccessible(true);
        tGroupField.set(requestData, 202);

        var reasonField = ChangeRequestDTO.class.getDeclaredField("reason");
        reasonField.setAccessible(true);
        reasonField.set(requestData, "Cambio por choque de horarios");
    }

    @Test
    void testReceiveStudentRequest() {
        Request req = service.receiveStudentRequest(requestData);

        assertAll("Receive Student Request",
                () -> assertNotNull(req.getId()),
                () -> assertEquals(RequestType.REQUEST_FOR_GROUP_CHANGE, req.getType()),
                () -> assertEquals(student, req.getStudent()),
                () -> assertEquals(originalSubject, req.getOriginalSubject()),
                () -> assertEquals(targetSubject, req.getTargetSubject()),
                () -> assertEquals(101, req.getOriginalGroup()),
                () -> assertEquals(202, req.getTargetGroup()),
                () -> assertEquals(RequestStatus.PENDING, req.getStatus()),
                () -> assertNotNull(req.getCreationDate())
        );
    }



    @Test
    void testRouteRequestToFacultyFalse() {
        boolean routed = service.routeRequestToFaculty(UUID.randomUUID().toString());

        assertFalse(routed, "No debe enrutar si el ID no existe");
    }


    @Test
    void testValidateCapacityExceeded() throws Exception {
        // Cambiar el número de registrados para exceder
        var registeredField = Subject.class.getDeclaredField("registered");
        registeredField.setAccessible(true);
        registeredField.set(targetSubject, 35);

        Request req = service.receiveStudentRequest(requestData);
        boolean valid = service.validateCapacity(req.getId());

        assertFalse(valid, "Debe ser falso porque no hay cupos");
    }



    @Test
    void testReceiveStudentRequestGeneratesUniqueId() {
        Request req1 = service.receiveStudentRequest(requestData);
        Request req2 = service.receiveStudentRequest(requestData);

        assertNotEquals(req1.getId(), req2.getId(),
                "Cada request debe generar un ID único");
    }

    @Test
    void testReceiveStudentRequestStatusIsPending() {
        Request req = service.receiveStudentRequest(requestData);

        assertEquals(RequestStatus.PENDING, req.getStatus(),
                "Al recibir una solicitud nueva, el estado debe ser PENDING");
    }

    @Test
    void testRouteRequestWithNullId() {
        boolean routed = service.routeRequestToFaculty(null);

        assertFalse(routed, "No debe enrutar cuando el ID es null");
    }

    @Test
    void testValidateScheduleOverlapReturnsFalseForSameTimes() throws Exception {
        var classTimeField = Subject.class.getDeclaredField("classTime");
        classTimeField.setAccessible(true);
        classTimeField.set(originalSubject, "10:00");

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateScheduleOverlap(request.getId());
        assertFalse(result);
    }

    @Test
    void testValidateCapacityWithNullTargetSubjectReturnsFalse() throws Exception {
        var tSubjectField = ChangeRequestDTO.class.getDeclaredField("targetSubject");
        tSubjectField.setAccessible(true);
        tSubjectField.set(requestData, null);

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateCapacity(request.getId());
        assertFalse(result);
    }

    @Test
    void testValidateScheduleOverlapWithNullSubjectsReturnsFalse() throws Exception {
        var oSubjectField = ChangeRequestDTO.class.getDeclaredField("originalSubject");
        oSubjectField.setAccessible(true);
        oSubjectField.set(requestData, null);

        var tSubjectField = ChangeRequestDTO.class.getDeclaredField("targetSubject");
        tSubjectField.setAccessible(true);
        tSubjectField.set(requestData, null);

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateScheduleOverlap(request.getId());
        assertFalse(result);
    }

    @Test
    void testValidateScheduleOverlapWithNullOriginalSubject() throws Exception {
        var oSubjectField = ChangeRequestDTO.class.getDeclaredField("originalSubject");
        oSubjectField.setAccessible(true);
        oSubjectField.set(requestData, null);

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateScheduleOverlap(request.getId());
        assertFalse(result);
    }

    @Test
    void testValidateScheduleOverlapWithNullTargetSubject() throws Exception {
        var tSubjectField = ChangeRequestDTO.class.getDeclaredField("targetSubject");
        tSubjectField.setAccessible(true);
        tSubjectField.set(requestData, null);

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateScheduleOverlap(request.getId());
        assertFalse(result);
    }

    @Test
    void testValidateCapacityWithExactQuotas() throws Exception {
        var registeredField = Subject.class.getDeclaredField("registered");
        registeredField.setAccessible(true);
        registeredField.set(targetSubject, 30);

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateCapacity(request.getId());
        assertFalse(result);
    }

    @Test
    void testValidateCapacityWithZeroQuotas() throws Exception {
        var quotasField = Subject.class.getDeclaredField("quotas");
        quotasField.setAccessible(true);
        quotasField.set(targetSubject, 0);

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateCapacity(request.getId());
        assertFalse(result);
    }

    @Test
    void testValidateScheduleOverlapWithEmptyClassTime() throws Exception {
        var classTimeField = Subject.class.getDeclaredField("classTime");
        classTimeField.setAccessible(true);
        classTimeField.set(originalSubject, "");
        classTimeField.set(targetSubject, "");

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateScheduleOverlap(request.getId());
        assertFalse(result);
    }

    @Test
    void testAssignAutomaticPriorityMaintainsOtherProperties() {
        Request originalRequest = service.receiveStudentRequest(requestData);
        Request prioritizedRequest = service.assignAutomaticPriority(originalRequest);

        assertEquals(originalRequest.getType(), prioritizedRequest.getType());
        assertEquals(originalRequest.getStudent(), prioritizedRequest.getStudent());
        assertEquals(originalRequest.getOriginalSubject(), prioritizedRequest.getOriginalSubject());
        assertEquals(originalRequest.getTargetSubject(), prioritizedRequest.getTargetSubject());
    }

    @Test
    void testMultipleAssignAutomaticPriorityCalls() {
        Request request = service.receiveStudentRequest(requestData);
        Request prioritized1 = service.assignAutomaticPriority(request);
        Request prioritized2 = service.assignAutomaticPriority(prioritized1);

        assertEquals(prioritized1.getPriority(), prioritized2.getPriority());
    }

    @Test
    void testValidateCapacityWithNegativeRegistered() throws Exception {
        var registeredField = Subject.class.getDeclaredField("registered");
        registeredField.setAccessible(true);
        registeredField.set(targetSubject, -5);

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateCapacity(request.getId());
        assertFalse(result);
    }

    @Test
    void testValidateCapacityWithNegativeQuotas() throws Exception {
        var quotasField = Subject.class.getDeclaredField("quotas");
        quotasField.setAccessible(true);
        quotasField.set(targetSubject, -10);

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateCapacity(request.getId());
        assertFalse(result);
    }

    @Test
    void testValidateScheduleOverlapWithNullClassTime() throws Exception {
        var classTimeField = Subject.class.getDeclaredField("classTime");
        classTimeField.setAccessible(true);
        classTimeField.set(originalSubject, null);
        classTimeField.set(targetSubject, null);

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateScheduleOverlap(request.getId());
        assertFalse(result);
    }

    @Test
    void testGetRequestAuditTrailFormat() {
        Request request = service.receiveStudentRequest(requestData);
        List<String> auditTrail = service.getRequestAuditTrail(request.getId());

        for (String entry : auditTrail) {
            assertNotNull(entry);
            assertFalse(entry.trim().isEmpty());
        }
    }

    @Test
    void testRouteRequestToFacultyWithEmptyStudentId() throws Exception {
        var studentField = Student.class.getDeclaredField("id");
        studentField.setAccessible(true);
        studentField.set(student, "");

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.routeRequestToFaculty(request.getId());
        assertFalse(result);
    }

    @Test
    void testRouteRequestToFacultyWithNullStudentId() throws Exception {
        var studentField = Student.class.getDeclaredField("id");
        studentField.setAccessible(true);
        studentField.set(student, null);

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.routeRequestToFaculty(request.getId());
        assertFalse(result);
    }

    @Test
    void testValidateScheduleOverlapWithDifferentFormats() throws Exception {
        var classTimeField = Subject.class.getDeclaredField("classTime");
        classTimeField.setAccessible(true);
        classTimeField.set(originalSubject, "08:00 AM");
        classTimeField.set(targetSubject, "08:00");

        Request request = service.receiveStudentRequest(requestData);
        boolean result = service.validateScheduleOverlap(request.getId());
        assertFalse(result);
    }

    @Test
    void testRequestCreationDateIsRecent() {
        Request request = service.receiveStudentRequest(requestData);
        Date now = new Date();
        long timeDifference = now.getTime() - request.getCreationDate().getTime();
        assertTrue(timeDifference < 1000);
    }

    @Test
    void testRequestSolveDateIsNullForNewRequests() {
        Request request = service.receiveStudentRequest(requestData);
        assertNull(request.getSolveDate());
    }

    @Test
    void testRequestObservationsMatchInput() {
        Request request = service.receiveStudentRequest(requestData);
        assertEquals("Cambio por choque de horarios", request.getObservations());
    }

    @Test
    void testRequestGroupsAreCorrect() {
        Request request = service.receiveStudentRequest(requestData);
        assertEquals(101, request.getOriginalGroup());
        assertEquals(202, request.getTargetGroup());
    }

    @Test
    void testRequestTypeConversion() {
        Request request = service.receiveStudentRequest(requestData);
        assertEquals(RequestType.REQUEST_FOR_GROUP_CHANGE, request.getType());
    }

    @Test
    void testRequestStatusIsAlwaysPending() {
        Request request = service.receiveStudentRequest(requestData);
        assertEquals(RequestStatus.PENDING, request.getStatus());
    }

    @Test
    void testRequestStudentCareer() throws Exception {
        Request request = service.receiveStudentRequest(requestData);
        assertEquals("Engineering", request.getStudent().getCareer());
    }

}