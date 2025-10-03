package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.DateRange;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicCycle;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentProgress;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicPerformance;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final List<Request> requests = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<Subject> subjects = new ArrayList<>();
    private final List<AcademicCycle> academicCycles = new ArrayList<>();

    public Map<String, Object> getApprovalRejectionStats(DateRange dateRange) {
        List<Request> filteredRequests = requests.stream()
                .filter(request -> isWithinDateRange(request.getCreationDate(), dateRange))
                .collect(Collectors.toList());

        long approved = filteredRequests.stream().filter(req -> req.getStatus() == RequestStatus.APPROVED).count();
        long rejected = filteredRequests.stream().filter(req -> req.getStatus() == RequestStatus.REJECTED).count();
        long pending = filteredRequests.stream().filter(req -> req.getStatus() == RequestStatus.PENDING).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", filteredRequests.size());
        stats.put("approved", approved);
        stats.put("rejected", rejected);
        stats.put("pending", pending);
        stats.put("approvalRate", filteredRequests.isEmpty() ? 0 : (double) approved / filteredRequests.size());
        return stats;
    }

    public Map<String, Object> getRequestVolumeStats(String timePeriod) {
        Map<String, Long> volumeByPeriod = requests.stream()
                .collect(Collectors.groupingBy(
                        request -> extractTimePeriod(request.getCreationDate(), timePeriod),
                        Collectors.counting()
                ));

        Map<String, Object> stats = new HashMap<>();
        stats.put("timePeriod", timePeriod);
        stats.put("volumeByPeriod", volumeByPeriod);
        stats.put("totalRequests", requests.size());
        return stats;
    }

    public Map<String, Object> getProcessingTimeStats() {
        List<Request> processedRequests = requests.stream()
                .filter(req -> req.getSolveDate() != null)
                .collect(Collectors.toList());

        OptionalDouble avgProcessingTime = processedRequests.stream()
                .mapToLong(req -> req.getSolveDate().getTime() - req.getCreationDate().getTime())
                .average();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProcessed", processedRequests.size());
        stats.put("avgProcessingTimeHours", avgProcessingTime.isPresent() ? avgProcessingTime.getAsDouble() / (1000 * 60 * 60) : 0);
        return stats;
    }

    public Map<String, Object> getFacultyApprovalStats(String faculty, DateRange dateRange) {
        List<Request> facultyRequests = requests.stream()
                .filter(request -> request.getStudent() != null && faculty.equals(request.getStudent().getCareer()))
                .filter(request -> isWithinDateRange(request.getCreationDate(), dateRange))
                .collect(Collectors.toList());

        long approved = facultyRequests.stream().filter(req -> req.getStatus() == RequestStatus.APPROVED).count();
        long rejected = facultyRequests.stream().filter(req -> req.getStatus() == RequestStatus.REJECTED).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("faculty", faculty);
        stats.put("totalRequests", facultyRequests.size());
        stats.put("approved", approved);
        stats.put("rejected", rejected);
        stats.put("approvalRate", facultyRequests.isEmpty() ? 0 : (double) approved / facultyRequests.size());
        return stats;
    }

    public List<Map<String, Object>> getFacultyMostRequestedSubjects(String faculty) {
        return requests.stream()
                .filter(request -> request.getStudent() != null && faculty.equals(request.getStudent().getCareer()))
                .filter(request -> request.getTargetSubject() != null)
                .collect(Collectors.groupingBy(
                        request -> request.getTargetSubject().getName(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> subjectStats = new HashMap<>();
                    subjectStats.put("subjectName", entry.getKey());
                    subjectStats.put("requestCount", entry.getValue());
                    return subjectStats;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getFacultyStudentPerformance(String faculty) {
        List<AcademicCycle> facultyCycles = academicCycles.stream()
                .filter(cycle -> {
                    return true;
                })
                .collect(Collectors.toList());

        OptionalDouble avgCumulative = facultyCycles.stream()
                .filter(cycle -> cycle.getAcademicPerformance() != null && cycle.getAcademicPerformance().getCumulativeAverage() != null)
                .mapToDouble(cycle -> cycle.getAcademicPerformance().getCumulativeAverage())
                .average();

        Map<String, Object> performance = new HashMap<>();
        performance.put("faculty", faculty);
        performance.put("totalStudents", facultyCycles.size());
        performance.put("avgCumulative", avgCumulative.isPresent() ? avgCumulative.getAsDouble() : 0);
        return performance;
    }

    public Map<String, Object> getFacultyCapacityUtilization(String faculty) {
        List<Subject> facultySubjects = subjects.stream()
                .filter(subject -> {
                    return true;
                })
                .collect(Collectors.toList());

        double avgUtilization = facultySubjects.stream()
                .mapToDouble(subject -> (double) subject.getRegistered() / subject.getQuotas())
                .average()
                .orElse(0);

        Map<String, Object> capacity = new HashMap<>();
        capacity.put("faculty", faculty);
        capacity.put("totalSubjects", facultySubjects.size());
        capacity.put("avgUtilization", avgUtilization);
        return capacity;
    }

    public Map<String, Object> getSystemWideApprovalStats() {
        long total = requests.size();
        long approved = requests.stream().filter(req -> req.getStatus() == RequestStatus.APPROVED).count();
        long rejected = requests.stream().filter(req -> req.getStatus() == RequestStatus.REJECTED).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRequests", total);
        stats.put("approved", approved);
        stats.put("rejected", rejected);
        stats.put("approvalRate", total == 0 ? 0 : (double) approved / total);
        return stats;
    }

    public List<Map<String, Object>> getGlobalMostRequestedSubjects() {
        return requests.stream()
                .filter(request -> request.getTargetSubject() != null)
                .collect(Collectors.groupingBy(
                        request -> request.getTargetSubject().getName(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> subjectStats = new HashMap<>();
                    subjectStats.put("subjectName", entry.getKey());
                    subjectStats.put("requestCount", entry.getValue());
                    return subjectStats;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getOverallAcademicProgress() {
        int totalStudents = students.size();
        int totalApprovedCredits = academicCycles.stream()
                .filter(cycle -> cycle.getStudentProgress() != null)
                .mapToInt(cycle -> cycle.getStudentProgress().getApprovedCredits())
                .sum();

        Map<String, Object> progress = new HashMap<>();
        progress.put("totalStudents", totalStudents);
        progress.put("totalApprovedCredits", totalApprovedCredits);
        progress.put("avgCreditsPerStudent", totalStudents == 0 ? 0 : totalApprovedCredits / totalStudents);
        return progress;
    }

    public Map<String, Object> getInstitutionalCapacityOverview() {
        int totalCapacity = subjects.stream().mapToInt(Subject::getQuotas).sum();
        int totalRegistered = subjects.stream().mapToInt(Subject::getRegistered).sum();
        double utilizationRate = totalCapacity == 0 ? 0 : (double) totalRegistered / totalCapacity;

        Map<String, Object> overview = new HashMap<>();
        overview.put("totalCapacity", totalCapacity);
        overview.put("totalRegistered", totalRegistered);
        overview.put("utilizationRate", utilizationRate);
        overview.put("availableSpots", totalCapacity - totalRegistered);
        return overview;
    }

    private boolean isWithinDateRange(Date date, DateRange dateRange) {
        return date != null && dateRange != null &&
                !date.before(dateRange.getStartDate()) &&
                !date.after(dateRange.getEndDate());
    }

    private String extractTimePeriod(Date date, String timePeriod) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        switch (timePeriod) {
            case "daily":
                return String.format("%04d-%02d-%02d",
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH));
            case "monthly":
                return String.format("%04d-%02d",
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1);
            case "yearly":
                return String.valueOf(cal.get(Calendar.YEAR));
            default:
                return "unknown";
        }
    }
}