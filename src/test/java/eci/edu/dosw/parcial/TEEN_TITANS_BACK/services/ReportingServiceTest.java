package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.StudentHistoryReportDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ReportingServiceTest {

    private ReportingService reportingService;

    @BeforeEach
    void setUp() {
        reportingService = new ReportingService();
    }

    private Request buildRequest(String id, Student student, Subject subject, RequestStatus status, Date creationDate) throws Exception {
        Request request = new Request();
        Field idField = Request.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(request, id);

        Field studentField = Request.class.getDeclaredField("student");
        studentField.setAccessible(true);
        studentField.set(request, student);

        Field subjectField = Request.class.getDeclaredField("targetSubject");
        subjectField.setAccessible(true);
        subjectField.set(request, subject);

        Field statusField = Request.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(request, status);

        Field dateField = Request.class.getDeclaredField("creationDate");
        dateField.setAccessible(true);
        dateField.set(request, creationDate);

        return request;
    }

    private Student buildStudent(String id, String career) throws Exception {
        Student student = new Student();
        Field idField = Student.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(student, id);

        Field careerField = Student.class.getDeclaredField("career");
        careerField.setAccessible(true);
        careerField.set(student, career);

        return student;
    }

    private Subject buildSubject(String name, int group) throws Exception {
        Subject subject = new Subject();
        Field nameField = Subject.class.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(subject, name);

        Field groupField = Subject.class.getDeclaredField("group");
        groupField.setAccessible(true);
        groupField.set(subject, group);

        return subject;
    }

    @Test
    void testGenerateStudentChangeHistory() throws Exception {
        Student student = buildStudent("stu-1", "Engineering");
        Subject subject = buildSubject("Math", 101);

        Request req1 = buildRequest("req-1", student, subject, RequestStatus.APPROVED, new Date());
        Request req2 = buildRequest("req-2", student, subject, RequestStatus.REJECTED, new Date());

        Field requestsField = ReportingService.class.getDeclaredField("requests");
        requestsField.setAccessible(true);
        List<Request> requests = (List<Request>) requestsField.get(reportingService);
        requests.add(req1);
        requests.add(req2);

        StudentHistoryReportDTO report = reportingService.generateStudentChangeHistory("stu-1");

        assertEquals("stu-1", report.getStudentId());
        assertEquals(2, report.getTotalRequests());
        assertEquals(1, report.getApprovedRequests());
        assertEquals(1, report.getRejectedRequests());
    }

    @Test
    void testGetMostRequestedGroups() throws Exception {
        Student student = buildStudent("stu-2", "Engineering");
        Subject subj1 = buildSubject("Physics", 202);
        Subject subj2 = buildSubject("Chemistry", 303);

        Request r1 = buildRequest("r1", student, subj1, RequestStatus.PENDING, new Date());
        Request r2 = buildRequest("r2", student, subj1, RequestStatus.PENDING, new Date());
        Request r3 = buildRequest("r3", student, subj2, RequestStatus.PENDING, new Date());

        Field requestsField = ReportingService.class.getDeclaredField("requests");
        requestsField.setAccessible(true);
        List<Request> requests = (List<Request>) requestsField.get(reportingService);
        requests.addAll(Arrays.asList(r1, r2, r3));

        List<Map<String, Object>> stats = reportingService.getMostRequestedGroups();

        assertEquals(2, stats.size());
        assertEquals(202, stats.get(0).get("groupId"));
        assertEquals(2L, stats.get(0).get("requestCount"));
    }

    @Test
    void testGetApprovalRejectionStats() throws Exception {
        Student student = buildStudent("stu-3", "Law");
        Subject subject = buildSubject("Constitution", 404);

        Request approved = buildRequest("r1", student, subject, RequestStatus.APPROVED, new Date());
        Request rejected = buildRequest("r2", student, subject, RequestStatus.REJECTED, new Date());
        Request pending = buildRequest("r3", student, subject, RequestStatus.PENDING, new Date());

        Field requestsField = ReportingService.class.getDeclaredField("requests");
        requestsField.setAccessible(true);
        List<Request> requests = (List<Request>) requestsField.get(reportingService);
        requests.addAll(Arrays.asList(approved, rejected, pending));

        ReportingService.DateRange range = new ReportingService.DateRange();
        range.setStartDate(new Date(System.currentTimeMillis() - 1000000));
        range.setEndDate(new Date(System.currentTimeMillis() + 1000000));

        Map<String, Object> stats = reportingService.getApprovalRejectionStats(range);

        assertEquals(3, stats.get("totalRequests"));
        assertEquals(1L, stats.get("approved"));
        assertEquals(1L, stats.get("rejected"));
        assertEquals(1L, stats.get("pending"));
        assertEquals(1.0 / 3, (double) stats.get("approvalRate"));
    }

    @Test
    void testGetGlobalAcademicProgress() throws Exception {
        Student s1 = buildStudent("stu-4", "Engineering");
        Student s2 = buildStudent("stu-5", "Law");
        Subject subj = buildSubject("History", 505);

        Request r1 = buildRequest("ra", s1, subj, RequestStatus.PENDING, new Date());
        Request r2 = buildRequest("rb", s2, subj, RequestStatus.REJECTED, new Date());

        Field studentsField = ReportingService.class.getDeclaredField("students");
        studentsField.setAccessible(true);
        List<Student> students = (List<Student>) studentsField.get(reportingService);
        students.addAll(Arrays.asList(s1, s2));

        Field requestsField = ReportingService.class.getDeclaredField("requests");
        requestsField.setAccessible(true);
        List<Request> requests = (List<Request>) requestsField.get(reportingService);
        requests.addAll(Arrays.asList(r1, r2));

        Map<String, Object> progress = reportingService.getGlobalAcademicProgress();

        assertEquals(2, progress.get("totalStudents"));
        assertEquals(2, progress.get("totalRequests"));
        assertEquals(1L, progress.get("activeRequests"));
        assertTrue(((Map<String, Long>) progress.get("requestsByFaculty")).containsKey("Engineering"));
    }

}
