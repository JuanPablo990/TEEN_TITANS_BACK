package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import java.util.List;

public class StudentHistoryReportDTO {
    private String studentId;
    private int totalRequests;
    private int approvedRequests;
    private int rejectedRequests;
    private List<Request> requestHistory;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public int getTotalRequests() { return totalRequests; }
    public void setTotalRequests(int totalRequests) { this.totalRequests = totalRequests; }
    public int getApprovedRequests() { return approvedRequests; }
    public void setApprovedRequests(int approvedRequests) { this.approvedRequests = approvedRequests; }
    public int getRejectedRequests() { return rejectedRequests; }
    public void setRejectedRequests(int rejectedRequests) { this.rejectedRequests = rejectedRequests; }
    public List<Request> getRequestHistory() { return requestHistory; }
    public void setRequestHistory(List<Request> requestHistory) { this.requestHistory = requestHistory; }
}