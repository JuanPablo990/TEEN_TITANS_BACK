package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.StudentHistoryReportDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportingService {

    private final List<Request> requests = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<Subject> subjects = new ArrayList<>();

    public StudentHistoryReportDTO generateStudentChangeHistory(String studentId) {
        List<Request> studentRequests = requests.stream()
                .filter(request -> request.getStudent() != null && request.getStudent().getId().equals(studentId))
                .collect(Collectors.toList());

        StudentHistoryReportDTO report = new StudentHistoryReportDTO();
        report.setStudentId(studentId);
        report.setTotalRequests(studentRequests.size());
        report.setApprovedRequests((int) studentRequests.stream().filter(req -> req.getStatus() == RequestStatus.APPROVED).count());
        report.setRejectedRequests((int) studentRequests.stream().filter(req -> req.getStatus() == RequestStatus.REJECTED).count());
        report.setRequestHistory(studentRequests);
        return report;
    }

    public List<Map<String, Object>> getMostRequestedGroups() {
        return requests.stream()
                .filter(request -> request.getTargetSubject() != null)
                .collect(Collectors.groupingBy(
                        request -> request.getTargetSubject().getGroup(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("groupId", entry.getKey());
                    stats.put("requestCount", entry.getValue());
                    return stats;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getApprovalRejectionStats(DateRange dateRange) {
        List<Request> filteredRequests = requests.stream()
                .filter(request -> isWithinDateRange(request.getCreationDate(), dateRange))
                .collect(Collectors.toList());

        long approved = filteredRequests.stream().filter(req -> req.getStatus() == RequestStatus.APPROVED).count();
        long rejected = filteredRequests.stream().filter(req -> req.getStatus() == RequestStatus.REJECTED).count();
        long pending = filteredRequests.stream().filter(req -> req.getStatus() == RequestStatus.PENDING).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRequests", filteredRequests.size());
        stats.put("approved", approved);
        stats.put("rejected", rejected);
        stats.put("pending", pending);
        stats.put("approvalRate", filteredRequests.isEmpty() ? 0 : (double) approved / filteredRequests.size());
        return stats;
    }

    public Map<String, Object> getGlobalAcademicProgress() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("totalStudents", students.size());
        progress.put("totalRequests", requests.size());
        progress.put("activeRequests", requests.stream().filter(req -> req.getStatus() == RequestStatus.PENDING).count());

        Map<String, Long> requestsByFaculty = requests.stream()
                .filter(req -> req.getStudent() != null && req.getStudent().getCareer() != null)
                .collect(Collectors.groupingBy(
                        req -> req.getStudent().getCareer(),
                        Collectors.counting()
                ));
        progress.put("requestsByFaculty", requestsByFaculty);

        return progress;
    }

    public Map<String, Object> generateFacultyReports(String faculty) {
        List<Request> facultyRequests = requests.stream()
                .filter(request -> request.getStudent() != null && faculty.equals(request.getStudent().getCareer()))
                .collect(Collectors.toList());

        Map<String, Object> report = new HashMap<>();
        report.put("faculty", faculty);
        report.put("totalRequests", facultyRequests.size());
        report.put("approvedRequests", facultyRequests.stream().filter(req -> req.getStatus() == RequestStatus.APPROVED).count());
        report.put("rejectedRequests", facultyRequests.stream().filter(req -> req.getStatus() == RequestStatus.REJECTED).count());
        report.put("pendingRequests", facultyRequests.stream().filter(req -> req.getStatus() == RequestStatus.PENDING).count());

        Map<Object, Long> requestsByType = facultyRequests.stream()
                .collect(Collectors.groupingBy(Request::getType, Collectors.counting()));
        report.put("requestsByType", requestsByType);

        return report;
    }

    private boolean isWithinDateRange(Date date, DateRange dateRange) {
        return date != null && dateRange != null &&
                !date.before(dateRange.getStartDate()) &&
                !date.after(dateRange.getEndDate());
    }

    public static class DateRange {
        private Date startDate;
        private Date endDate;

        public Date getStartDate() { return startDate; }
        public void setStartDate(Date startDate) { this.startDate = startDate; }
        public Date getEndDate() { return endDate; }
        public void setEndDate(Date endDate) { this.endDate = endDate; }
    }
}