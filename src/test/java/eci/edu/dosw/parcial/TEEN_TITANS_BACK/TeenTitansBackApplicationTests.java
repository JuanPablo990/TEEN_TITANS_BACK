package eci.edu.dosw.parcial.TEEN_TITANS_BACK;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.main.web-application-type=none",
		"spring.main.banner-mode=off",
		"logging.level.root=ERROR"
})
class TeenTitansBackApplicationTests {

	@Test
	@DisplayName("Caso exitoso - aplicación inicia con SpringBootTest")
	void testApplicationStartsWithSpringBootTest() {
		assertDoesNotThrow(() -> {
			TeenTitansBackApplication.main(new String[] {
					"--spring.main.web-application-type=none",
					"--spring.main.banner-mode=off"
			});
		});
	}

	@Test
	@DisplayName("Caso borde - verificar que es una clase pública")
	void testClassIsPublic() {
		int modifiers = TeenTitansBackApplication.class.getModifiers();

		assertAll("Verificar modificadores de la clase",
				() -> assertTrue(java.lang.reflect.Modifier.isPublic(modifiers)),
				() -> assertFalse(java.lang.reflect.Modifier.isAbstract(modifiers)),
				() -> assertFalse(java.lang.reflect.Modifier.isFinal(modifiers))
		);
	}

	@Test
	@DisplayName("Caso borde - constructor por defecto funciona")
	void testConstructorPorDefecto() {
		assertDoesNotThrow(() -> {
			TeenTitansBackApplication app = new TeenTitansBackApplication();
			assertNotNull(app);
		});
	}
}