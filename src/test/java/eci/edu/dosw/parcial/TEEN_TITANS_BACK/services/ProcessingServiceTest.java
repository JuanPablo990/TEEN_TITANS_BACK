package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.ChangeRequestDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


}