package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class AcademicCycleTest {

    private AcademicCycle cycle;

    @BeforeEach
    void setUp() {
        cycle = new AcademicCycle();
    }

    @Test
    void testInitialValues() {
        assertAll("Valores iniciales por defecto",
                () -> assertEquals(0, cycle.getCycleId()),
                () -> assertEquals(0, cycle.getSemesterToAttend()),
                () -> assertEquals(0, cycle.getLastSemesterAttended()),
                () -> assertNull(cycle.getAdmissionCycle()),
                () -> assertNull(cycle.getAcademicSituation()),
                () -> assertNull(cycle.getProgramStatus()),
                () -> assertNull(cycle.getActionReason()),
                () -> assertNull(cycle.getAcademicCalendar()),
                () -> assertNull(cycle.getAcademicPerformance()),
                () -> assertNull(cycle.getStudentProgress())
        );
    }

    @Test
    void testSetAndGetPrimitiveValues() throws Exception {
        setPrivateField("cycleId", 101);
        setPrivateField("semesterToAttend", 5);
        setPrivateField("lastSemesterAttended", 4);

        assertAll("Valores primitivos",
                () -> assertEquals(101, cycle.getCycleId()),
                () -> assertEquals(5, cycle.getSemesterToAttend()),
                () -> assertEquals(4, cycle.getLastSemesterAttended())
        );
    }

    @Test
    void testSetAndGetStringValues() throws Exception {
        setPrivateField("admissionCycle", "2025-1");
        setPrivateField("academicSituation", "Activo");
        setPrivateField("programStatus", "En progreso");
        setPrivateField("actionReason", "Cambio de plan");

        assertAll("Cadenas",
                () -> assertEquals("2025-1", cycle.getAdmissionCycle()),
                () -> assertEquals("Activo", cycle.getAcademicSituation()),
                () -> assertEquals("En progreso", cycle.getProgramStatus()),
                () -> assertEquals("Cambio de plan", cycle.getActionReason())
        );
    }

    @Test
    void testSetAndGetObjectReferences() throws Exception {
        AcademicCalendar calendar = new AcademicCalendar();
        AcademicPerformance performance = new AcademicPerformance(); // suponiendo que existe
        StudentProgress progress = new StudentProgress(); // suponiendo que existe

        setPrivateField("academicCalendar", calendar);
        setPrivateField("academicPerformance", performance);
        setPrivateField("studentProgress", progress);

        assertAll("Objetos relacionados",
                () -> assertSame(calendar, cycle.getAcademicCalendar()),
                () -> assertSame(performance, cycle.getAcademicPerformance()),
                () -> assertSame(progress, cycle.getStudentProgress())
        );
    }


    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = AcademicCycle.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(cycle, value);
    }
}
