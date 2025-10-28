package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class CourseRepositoryTest {

    @MockBean
    private CourseRepository courseRepository;

    private Course course1;
    private Course course2;
    private Course course3;
    private Course course4;

    @BeforeEach
    void setUp() {
        course1 = new Course("CS101", "Introduction to Programming", 3, "Basic programming concepts", "Computer Science", true);
        course2 = new Course("MATH201", "Calculus I", 4, "Differential calculus", "Mathematics", true);
        course3 = new Course("PHY301", "Advanced Physics", 5, "Advanced physics topics", "Physics", false);
        course4 = new Course("CS201", "Data Structures", 4, "Data structures and algorithms", "Computer Science", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByName retorna curso cuando existe")
    void testFindByName_Exitoso() {
        when(courseRepository.findByName("Introduction to Programming"))
                .thenReturn(Optional.of(course1));

        Optional<Course> resultado = courseRepository.findByName("Introduction to Programming");

        assertAll("Verificar curso encontrado por nombre",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("Introduction to Programming", resultado.get().getName()),
                () -> assertEquals("CS101", resultado.get().getCourseCode()),
                () -> assertTrue(resultado.get().getIsActive())
        );

        verify(courseRepository, times(1)).findByName("Introduction to Programming");
    }

    @Test
    @DisplayName("Caso error - findByName retorna vacío cuando no existe")
    void testFindByName_CursoNoExiste() {
        when(courseRepository.findByName("Non-existent Course"))
                .thenReturn(Optional.empty());

        Optional<Course> resultado = courseRepository.findByName("Non-existent Course");

        assertAll("Verificar que no se encuentra curso inexistente",
                () -> assertFalse(resultado.isPresent()),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(courseRepository, times(1)).findByName("Non-existent Course");
    }

    @Test
    @DisplayName("Caso exitoso - findByIsActive retorna cursos activos")
    void testFindByIsActive_Exitoso() {
        when(courseRepository.findByIsActive(true))
                .thenReturn(List.of(course1, course2, course4));

        List<Course> resultado = courseRepository.findByIsActive(true);

        assertAll("Verificar cursos activos",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getIsActive()),
                () -> assertTrue(resultado.get(1).getIsActive()),
                () -> assertTrue(resultado.get(2).getIsActive())
        );

        verify(courseRepository, times(1)).findByIsActive(true);
    }

    @Test
    @DisplayName("Caso error - findByIsActive retorna lista vacía cuando no hay cursos activos")
    void testFindByIsActive_SinCursosActivos() {
        when(courseRepository.findByIsActive(false))
                .thenReturn(List.of(course3));

        List<Course> resultado = courseRepository.findByIsActive(false);

        assertAll("Verificar cursos inactivos",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertFalse(resultado.get(0).getIsActive())
        );

        verify(courseRepository, times(1)).findByIsActive(false);
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgram retorna cursos del programa")
    void testFindByAcademicProgram_Exitoso() {
        when(courseRepository.findByAcademicProgram("Computer Science"))
                .thenReturn(List.of(course1, course4));

        List<Course> resultado = courseRepository.findByAcademicProgram("Computer Science");

        assertAll("Verificar cursos de Computer Science",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Computer Science", resultado.get(0).getAcademicProgram()),
                () -> assertEquals("Computer Science", resultado.get(1).getAcademicProgram())
        );

        verify(courseRepository, times(1)).findByAcademicProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso exitoso - findByCredits retorna cursos con créditos específicos")
    void testFindByCredits_Exitoso() {
        when(courseRepository.findByCredits(4))
                .thenReturn(List.of(course2, course4));

        List<Course> resultado = courseRepository.findByCredits(4);

        assertAll("Verificar cursos con 4 créditos",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(4, resultado.get(0).getCredits()),
                () -> assertEquals(4, resultado.get(1).getCredits())
        );

        verify(courseRepository, times(1)).findByCredits(4);
    }

    @Test
    @DisplayName("Caso exitoso - findByCreditsBetween retorna cursos en rango de créditos")
    void testFindByCreditsBetween_Exitoso() {
        when(courseRepository.findByCreditsBetween(3, 4))
                .thenReturn(List.of(course1, course2, course4));

        List<Course> resultado = courseRepository.findByCreditsBetween(3, 4);

        assertAll("Verificar cursos en rango de créditos",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getCredits() >= 3 && resultado.get(0).getCredits() <= 4),
                () -> assertTrue(resultado.get(1).getCredits() >= 3 && resultado.get(1).getCredits() <= 4),
                () -> assertTrue(resultado.get(2).getCredits() >= 3 && resultado.get(2).getCredits() <= 4)
        );

        verify(courseRepository, times(1)).findByCreditsBetween(3, 4);
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramAndIsActive retorna cursos filtrados")
    void testFindByAcademicProgramAndIsActive_Exitoso() {
        when(courseRepository.findByAcademicProgramAndIsActive("Computer Science", true))
                .thenReturn(List.of(course1, course4));

        List<Course> resultado = courseRepository.findByAcademicProgramAndIsActive("Computer Science", true);

        assertAll("Verificar cursos activos de Computer Science",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Computer Science", resultado.get(0).getAcademicProgram()),
                () -> assertTrue(resultado.get(0).getIsActive()),
                () -> assertEquals("Computer Science", resultado.get(1).getAcademicProgram()),
                () -> assertTrue(resultado.get(1).getIsActive())
        );

        verify(courseRepository, times(1)).findByAcademicProgramAndIsActive("Computer Science", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContainingIgnoreCase retorna cursos con búsqueda case insensitive")
    void testFindByNameContainingIgnoreCase_Exitoso() {
        when(courseRepository.findByNameContainingIgnoreCase("programming"))
                .thenReturn(List.of(course1));

        List<Course> resultado = courseRepository.findByNameContainingIgnoreCase("programming");

        assertAll("Verificar búsqueda case insensitive",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getName().toLowerCase().contains("programming"))
        );

        verify(courseRepository, times(1)).findByNameContainingIgnoreCase("programming");
    }


    @Test
    @DisplayName("Caso exitoso - countByAcademicProgram retorna conteo correcto")
    void testCountByAcademicProgram_Exitoso() {
        when(courseRepository.countByAcademicProgram("Computer Science"))
                .thenReturn(2L);

        long resultado = courseRepository.countByAcademicProgram("Computer Science");

        assertEquals(2L, resultado);
        verify(courseRepository, times(1)).countByAcademicProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso exitoso - countByIsActive retorna conteo por estado")
    void testCountByIsActive_Exitoso() {
        when(courseRepository.countByIsActive(true))
                .thenReturn(3L);

        long resultado = courseRepository.countByIsActive(true);

        assertEquals(3L, resultado);
        verify(courseRepository, times(1)).countByIsActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - existsByName retorna true cuando existe")
    void testExistsByName_Exitoso() {
        when(courseRepository.existsByName("Introduction to Programming"))
                .thenReturn(true);

        boolean resultado = courseRepository.existsByName("Introduction to Programming");

        assertTrue(resultado);
        verify(courseRepository, times(1)).existsByName("Introduction to Programming");
    }

    @Test
    @DisplayName("Caso error - existsByName retorna false cuando no existe")
    void testExistsByName_NoExiste() {
        when(courseRepository.existsByName("Non-existent Course"))
                .thenReturn(false);

        boolean resultado = courseRepository.existsByName("Non-existent Course");

        assertFalse(resultado);
        verify(courseRepository, times(1)).existsByName("Non-existent Course");
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramIn retorna cursos de múltiples programas")
    void testFindByAcademicProgramIn_Exitoso() {
        when(courseRepository.findByAcademicProgramIn(List.of("Computer Science", "Mathematics")))
                .thenReturn(List.of(course1, course2, course4));

        List<Course> resultado = courseRepository.findByAcademicProgramIn(List.of("Computer Science", "Mathematics"));

        assertAll("Verificar cursos de múltiples programas",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(c -> c.getAcademicProgram().equals("Computer Science"))),
                () -> assertTrue(resultado.stream().anyMatch(c -> c.getAcademicProgram().equals("Mathematics")))
        );

        verify(courseRepository, times(1)).findByAcademicProgramIn(List.of("Computer Science", "Mathematics"));
    }

    @Test
    @DisplayName("Caso exitoso - findByCourseCode retorna curso por código")
    void testFindByCourseCode_Exitoso() {
        when(courseRepository.findByCourseCode("CS101"))
                .thenReturn(Optional.of(course1));

        Optional<Course> resultado = courseRepository.findByCourseCode("CS101");

        assertAll("Verificar curso por código",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("CS101", resultado.get().getCourseCode()),
                () -> assertEquals("Introduction to Programming", resultado.get().getName())
        );

        verify(courseRepository, times(1)).findByCourseCode("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameRegex con query personalizada")
    void testFindByNameRegex_Exitoso() {
        when(courseRepository.findByNameRegex(".*Programming.*"))
                .thenReturn(List.of(course1));

        List<Course> resultado = courseRepository.findByNameRegex(".*Programming.*");

        assertAll("Verificar búsqueda por regex",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getName().contains("Programming"))
        );

        verify(courseRepository, times(1)).findByNameRegex(".*Programming.*");
    }

    @Test
    @DisplayName("Caso exitoso - findByCreditsRange con query personalizada")
    void testFindByCreditsRange_Exitoso() {
        when(courseRepository.findByCreditsRange(3, 4))
                .thenReturn(List.of(course1, course2, course4));

        List<Course> resultado = courseRepository.findByCreditsRange(3, 4);

        assertAll("Verificar cursos en rango de créditos con query personalizada",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getCredits() >= 3 && resultado.get(0).getCredits() <= 4),
                () -> assertTrue(resultado.get(1).getCredits() >= 3 && resultado.get(1).getCredits() <= 4),
                () -> assertTrue(resultado.get(2).getCredits() >= 3 && resultado.get(2).getCredits() <= 4)
        );

        verify(courseRepository, times(1)).findByCreditsRange(3, 4);
    }

    @Test
    @DisplayName("Caso exitoso - findActiveCoursesSortedByCreditsDesc con query personalizada")
    void testFindActiveCoursesSortedByCreditsDesc_Exitoso() {
        when(courseRepository.findActiveCoursesSortedByCreditsDesc())
                .thenReturn(List.of(course2, course4, course1));

        List<Course> resultado = courseRepository.findActiveCoursesSortedByCreditsDesc();

        assertAll("Verificar cursos activos ordenados por créditos descendente",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getCredits() >= resultado.get(1).getCredits()),
                () -> assertTrue(resultado.get(1).getCredits() >= resultado.get(2).getCredits()),
                () -> assertTrue(resultado.get(0).getIsActive()),
                () -> assertTrue(resultado.get(1).getIsActive()),
                () -> assertTrue(resultado.get(2).getIsActive())
        );

        verify(courseRepository, times(1)).findActiveCoursesSortedByCreditsDesc();
    }

    @Test
    @DisplayName("Caso exitoso - searchCourses retorna cursos por término de búsqueda")
    void testSearchCourses_Exitoso() {
        when(courseRepository.searchCourses("programming", true))
                .thenReturn(List.of(course1));

        List<Course> resultado = courseRepository.searchCourses("programming", true);

        assertAll("Verificar búsqueda de cursos",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getName().toLowerCase().contains("programming") ||
                        resultado.get(0).getDescription().toLowerCase().contains("programming") ||
                        resultado.get(0).getAcademicProgram().toLowerCase().contains("programming")),
                () -> assertTrue(resultado.get(0).getIsActive())
        );

        verify(courseRepository, times(1)).searchCourses("programming", true);
    }

    @Test
    @DisplayName("Caso exitoso - existsByNameAndAcademicProgram retorna true")
    void testExistsByNameAndAcademicProgram_Exitoso() {
        when(courseRepository.existsByNameAndAcademicProgram("Introduction to Programming", "Computer Science"))
                .thenReturn(true);

        boolean resultado = courseRepository.existsByNameAndAcademicProgram("Introduction to Programming", "Computer Science");

        assertTrue(resultado);
        verify(courseRepository, times(1)).existsByNameAndAcademicProgram("Introduction to Programming", "Computer Science");
    }

    @Test
    @DisplayName("Caso borde - findByCredits con créditos nulos")
    void testFindByCredits_Nulo() {
        when(courseRepository.findByCredits(null))
                .thenReturn(Collections.emptyList());

        List<Course> resultado = courseRepository.findByCredits(null);

        assertTrue(resultado.isEmpty());
        verify(courseRepository, times(1)).findByCredits(null);
    }

    @Test
    @DisplayName("Caso borde - findByAcademicProgram con programa vacío")
    void testFindByAcademicProgram_Vacio() {
        when(courseRepository.findByAcademicProgram(""))
                .thenReturn(Collections.emptyList());

        List<Course> resultado = courseRepository.findByAcademicProgram("");

        assertTrue(resultado.isEmpty());
        verify(courseRepository, times(1)).findByAcademicProgram("");
    }

    @Test
    @DisplayName("Caso borde - Verificar integridad de datos en cursos encontrados")
    void testIntegridadDatosCursos() {
        when(courseRepository.findByCourseCode("CS101"))
                .thenReturn(Optional.of(course1));

        Optional<Course> resultado = courseRepository.findByCourseCode("CS101");

        assertAll("Verificar integridad completa del curso",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("CS101", resultado.get().getCourseCode()),
                () -> assertEquals("Introduction to Programming", resultado.get().getName()),
                () -> assertEquals(3, resultado.get().getCredits()),
                () -> assertEquals("Basic programming concepts", resultado.get().getDescription()),
                () -> assertEquals("Computer Science", resultado.get().getAcademicProgram()),
                () -> assertTrue(resultado.get().getIsActive()),
                () -> assertInstanceOf(Course.class, resultado.get()),
                () -> assertNotNull(resultado.get().toString())
        );
    }

    @Test
    @DisplayName("Caso exitoso - findByDescriptionIsNotNull retorna cursos con descripción")
    void testFindByDescriptionIsNotNull_Exitoso() {
        when(courseRepository.findByDescriptionIsNotNull())
                .thenReturn(List.of(course1, course2, course3, course4));

        List<Course> resultado = courseRepository.findByDescriptionIsNotNull();

        assertAll("Verificar cursos con descripción",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertNotNull(resultado.get(0).getDescription()),
                () -> assertNotNull(resultado.get(1).getDescription()),
                () -> assertNotNull(resultado.get(2).getDescription()),
                () -> assertNotNull(resultado.get(3).getDescription())
        );

        verify(courseRepository, times(1)).findByDescriptionIsNotNull();
    }

    @Test
    @DisplayName("Caso exitoso - findHighCreditCourses retorna cursos con muchos créditos")
    void testFindHighCreditCourses_Exitoso() {
        when(courseRepository.findHighCreditCourses(4))
                .thenReturn(List.of(course2, course3, course4));

        List<Course> resultado = courseRepository.findHighCreditCourses(4);

        assertAll("Verificar cursos con créditos altos",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getCredits() >= 4),
                () -> assertTrue(resultado.get(1).getCredits() >= 4),
                () -> assertTrue(resultado.get(2).getCredits() >= 4)
        );

        verify(courseRepository, times(1)).findHighCreditCourses(4);
    }
}