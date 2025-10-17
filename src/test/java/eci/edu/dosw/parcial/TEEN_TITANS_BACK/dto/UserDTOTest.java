package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void testBasicConstructor_Valid() {
        Date now = new Date();
        Date later = new Date(now.getTime() + 100000);

        UserDTO user = new UserDTO("USER001", "John Doe", "john@university.edu", "password123",
                "STUDENT", true, now, later);

        assertEquals("USER001", user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@university.edu", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("STUDENT", user.getRole());
        assertTrue(user.getActive());
        assertEquals(now, user.getCreatedAt());
        assertEquals(later, user.getUpdatedAt());
    }

    @Test
    void testBasicConstructor_NullValues() {
        UserDTO user = new UserDTO(null, null, null, null, null, null, null, null);

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getRole());
        assertNull(user.getActive());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    void testFullConstructor_Valid() {
        Date now = new Date();
        Date later = new Date(now.getTime() + 100000);
        List<String> expertise = List.of("AI", "Machine Learning");

        UserDTO user = new UserDTO("PROF001", "Dr. Smith", "smith@university.edu", "pass123",
                "PROFESSOR", true, now, later, "Computer Science", 0, 0.0,
                "Computer Science", true, expertise, "Engineering",
                "Building A-101", "IT Department");

        assertEquals("PROF001", user.getId());
        assertEquals("Dr. Smith", user.getName());
        assertEquals("smith@university.edu", user.getEmail());
        assertEquals("pass123", user.getPassword());
        assertEquals("PROFESSOR", user.getRole());
        assertTrue(user.getActive());
        assertEquals(now, user.getCreatedAt());
        assertEquals(later, user.getUpdatedAt());
        assertEquals("Computer Science", user.getAcademicProgram());
        assertEquals(0, user.getSemester());
        assertEquals(0.0, user.getGradeAverage());
        assertEquals("Computer Science", user.getProfessorDepartment());
        assertTrue(user.getIsTenured());
        assertEquals(2, user.getAreasOfExpertise().size());
        assertEquals("Engineering", user.getFaculty());
        assertEquals("Building A-101", user.getOfficeLocation());
        assertEquals("IT Department", user.getAdminDepartment());
    }

    @Test
    void testFullConstructor_NullValues() {
        UserDTO user = new UserDTO(null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null);

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getRole());
        assertNull(user.getActive());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
        assertNull(user.getAcademicProgram());
        assertNull(user.getSemester());
        assertNull(user.getGradeAverage());
        assertNull(user.getProfessorDepartment());
        assertNull(user.getIsTenured());
        assertNull(user.getAreasOfExpertise());
        assertNull(user.getFaculty());
        assertNull(user.getOfficeLocation());
        assertNull(user.getAdminDepartment());
    }

    @Test
    void testAddAreaOfExpertise_FirstArea() {
        UserDTO user = new UserDTO();

        user.addAreaOfExpertise("Data Science");

        assertEquals(1, user.getAreasOfExpertise().size());
        assertEquals("Data Science", user.getAreasOfExpertise().get(0));
    }

    @Test
    void testAddAreaOfExpertise_MultipleAreas() {
        UserDTO user = new UserDTO();

        user.addAreaOfExpertise("Mathematics");
        user.addAreaOfExpertise("Physics");
        user.addAreaOfExpertise("Chemistry");

        assertEquals(3, user.getAreasOfExpertise().size());
        assertEquals("Mathematics", user.getAreasOfExpertise().get(0));
        assertEquals("Physics", user.getAreasOfExpertise().get(1));
        assertEquals("Chemistry", user.getAreasOfExpertise().get(2));
    }

    @Test
    void testIsRole_True() {
        UserDTO user = new UserDTO();
        user.setRole("ADMIN");

        boolean result = user.isRole("ADMIN");

        assertTrue(result);
    }

    @Test
    void testIsRole_False() {
        UserDTO user = new UserDTO();
        user.setRole("STUDENT");

        boolean result = user.isRole("ADMIN");

        assertFalse(result);
    }

    @Test
    void testUpdateTimestamp_Valid() {
        UserDTO user = new UserDTO();
        user.setUpdatedAt(new Date(System.currentTimeMillis() - 100000));

        Date beforeUpdate = user.getUpdatedAt();
        user.updateTimestamp();
        Date afterUpdate = user.getUpdatedAt();

        assertNotNull(afterUpdate);
        assertTrue(afterUpdate.after(beforeUpdate));
    }

    @Test
    void testUpdateTimestamp_NullInitial() {
        UserDTO user = new UserDTO();
        user.setUpdatedAt(null);

        user.updateTimestamp();

        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void testSettersAndGetters_Valid() {
        UserDTO user = new UserDTO();
        Date now = new Date();
        Date later = new Date(now.getTime() + 100000);
        List<String> expertise = List.of("Networking", "Security");

        user.setId("USER002");
        user.setName("Jane Doe");
        user.setEmail("jane@university.edu");
        user.setPassword("newpass456");
        user.setRole("ADMIN");
        user.setActive(false);
        user.setCreatedAt(now);
        user.setUpdatedAt(later);
        user.setAcademicProgram("Electrical Engineering");
        user.setSemester(6);
        user.setGradeAverage(4.5);
        user.setProfessorDepartment("Electrical Engineering");
        user.setIsTenured(false);
        user.setAreasOfExpertise(expertise);
        user.setFaculty("Engineering");
        user.setOfficeLocation("Building B-205");
        user.setAdminDepartment("Administration");

        assertEquals("USER002", user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@university.edu", user.getEmail());
        assertEquals("newpass456", user.getPassword());
        assertEquals("ADMIN", user.getRole());
        assertFalse(user.getActive());
        assertEquals(now, user.getCreatedAt());
        assertEquals(later, user.getUpdatedAt());
        assertEquals("Electrical Engineering", user.getAcademicProgram());
        assertEquals(6, user.getSemester());
        assertEquals(4.5, user.getGradeAverage());
        assertEquals("Electrical Engineering", user.getProfessorDepartment());
        assertFalse(user.getIsTenured());
        assertEquals(2, user.getAreasOfExpertise().size());
        assertEquals("Engineering", user.getFaculty());
        assertEquals("Building B-205", user.getOfficeLocation());
        assertEquals("Administration", user.getAdminDepartment());
    }

    @Test
    void testSettersAndGetters_NullValues() {
        UserDTO user = new UserDTO();

        user.setId(null);
        user.setName(null);
        user.setEmail(null);
        user.setPassword(null);
        user.setRole(null);
        user.setActive(null);
        user.setCreatedAt(null);
        user.setUpdatedAt(null);
        user.setAcademicProgram(null);
        user.setSemester(null);
        user.setGradeAverage(null);
        user.setProfessorDepartment(null);
        user.setIsTenured(null);
        user.setAreasOfExpertise(null);
        user.setFaculty(null);
        user.setOfficeLocation(null);
        user.setAdminDepartment(null);

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getRole());
        assertNull(user.getActive());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
        assertNull(user.getAcademicProgram());
        assertNull(user.getSemester());
        assertNull(user.getGradeAverage());
        assertNull(user.getProfessorDepartment());
        assertNull(user.getIsTenured());
        assertNull(user.getAreasOfExpertise());
        assertNull(user.getFaculty());
        assertNull(user.getOfficeLocation());
        assertNull(user.getAdminDepartment());
    }
}