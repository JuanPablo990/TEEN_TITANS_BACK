package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AcademicPerformanceTest {

    private AcademicPerformance performance;

    @BeforeEach
    void setUp() {
        performance = new AcademicPerformance();
    }

    @Test
    void testInitialValuesAreNull() {
        assertAll("Valores iniciales",
                () -> assertNull(performance.getCumulativeAverage()),
                () -> assertNull(performance.getGradeAverage()),
                () -> assertNull(performance.getSemesterAverage())
        );
    }

    @Test
    void testSetAndGetCumulativeAverage() throws Exception {
        setPrivateField("cumulativeAverage", 3.75f);
        assertEquals(3.75f, performance.getCumulativeAverage());
    }

    @Test
    void testSetAndGetGradeAverage() throws Exception {
        setPrivateField("gradeAverage", 4.0f);
        assertNotEquals(3.0f, performance.getGradeAverage());
        assertEquals(4.0f, performance.getGradeAverage());
    }

    @Test
    void testSetAndGetSemesterAverage() throws Exception {
        List<Float> averages = Arrays.asList(3.5f, 4.0f, 4.2f);
        setPrivateField("semesterAverage", averages);

        assertAll("Lista de promedios semestrales",
                () -> assertNotNull(performance.getSemesterAverage()),
                () -> assertIterableEquals(averages, performance.getSemesterAverage())
        );
    }

    @Test
    void testDifferentInstancesNotSame() throws Exception {
        AcademicPerformance other = new AcademicPerformance();
        setPrivateField("cumulativeAverage", 3.5f);

        assertNotSame(other, performance);
        assertNotEquals(other.getCumulativeAverage(), performance.getCumulativeAverage());
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = AcademicPerformance.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(performance, value);
    }
}

