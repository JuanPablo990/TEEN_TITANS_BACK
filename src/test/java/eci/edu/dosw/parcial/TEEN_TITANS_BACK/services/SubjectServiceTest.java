package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.SubjectDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class SubjectServiceTest {

    private SubjectService subjectService;
    private SubjectDTO subjectDTO;
    private Administrator admin;
    private User nonAdmin;

    @BeforeEach
    void setUp() {
        subjectService = new SubjectService();

        subjectDTO = new SubjectDTO();
        subjectDTO.setName("Mathematics");
        subjectDTO.setCredits(4);
        subjectDTO.setQuotas(30);
        subjectDTO.setTeacher("Dr. Smith");
        subjectDTO.setClassTime("08:00-10:00");
        subjectDTO.setGroup(1);

        admin = new Administrator();
        nonAdmin = new User() {};
    }

    @Test
    void testCreateSubjectWithAdmin() {
        Subject result = subjectService.createSubject(subjectDTO, admin);

        assertNotNull(result);
    }

    @Test
    void testCreateSubjectWithNonAdmin() {
        Subject result = subjectService.createSubject(subjectDTO, nonAdmin);

        assertNull(result);
    }

    @Test
    void testGetSubjectExists() {
        subjectService.createSubject(subjectDTO, admin);
        String subjectId = "MATHEMATICS-1";

        Subject result = subjectService.getSubject(subjectId);

        assertNotNull(result);
    }

    @Test
    void testGetSubjectNotExists() {
        Subject result = subjectService.getSubject("nonexistent");

        assertNull(result);
    }

    @Test
    void testUpdateSubjectWithAdmin() {
        subjectService.createSubject(subjectDTO, admin);
        String subjectId = "MATHEMATICS-1";

        SubjectDTO updates = new SubjectDTO();
        updates.setName("Advanced Mathematics");

        Subject result = subjectService.updateSubject(subjectId, updates, admin);

        assertNotNull(result);
    }

    @Test
    void testUpdateSubjectWithNonAdmin() {
        SubjectDTO updates = new SubjectDTO();

        Subject result = subjectService.updateSubject("anyid", updates, nonAdmin);

        assertNull(result);
    }

    @Test
    void testDeleteSubjectWithAdmin() {
        subjectService.createSubject(subjectDTO, admin);
        String subjectId = "MATHEMATICS-1";

        boolean result = subjectService.deleteSubject(subjectId, admin);

        assertTrue(result);
    }

    @Test
    void testDeleteSubjectWithNonAdmin() {
        boolean result = subjectService.deleteSubject("anyid", nonAdmin);

        assertFalse(result);
    }

    @Test
    void testGetAllSubjectsEmpty() {
        var result = subjectService.getAllSubjects();

        assertEquals(0, result.size());
    }

    @Test
    void testGetAllSubjectsWithData() {
        subjectService.createSubject(subjectDTO, admin);

        var result = subjectService.getAllSubjects();

        assertFalse(result.isEmpty());
    }

    @Test
    void testAddGroupToSubjectWithAdmin() {
        subjectService.createSubject(subjectDTO, admin);
        String subjectId = "MATHEMATICS-1";

        SubjectDTO groupData = new SubjectDTO();

        Subject result = subjectService.addGroupToSubject(subjectId, groupData, admin);

        assertNotNull(result);
    }

    @Test
    void testRemoveGroupFromSubjectWithAdmin() {
        subjectService.createSubject(subjectDTO, admin);
        String subjectId = "MATHEMATICS-1";

        boolean result = subjectService.removeGroupFromSubject(subjectId, 1, admin);

        assertTrue(result);
    }

    @Test
    void testUpdateGroupCapacityWithAdmin() {
        subjectService.createSubject(subjectDTO, admin);
        String subjectId = "MATHEMATICS-1";

        boolean result = subjectService.updateGroupCapacity(subjectId, 1, 25, admin);

        assertTrue(result);
    }

    @Test
    void testSearchSubjectsByName() {
        subjectService.createSubject(subjectDTO, admin);

        var result = subjectService.searchSubjectsByName("math");

        assertFalse(result.isEmpty());
    }
}