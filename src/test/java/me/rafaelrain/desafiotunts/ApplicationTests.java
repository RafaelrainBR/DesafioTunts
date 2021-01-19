package me.rafaelrain.desafiotunts;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTests {

    private Application application;

    @BeforeAll
    public void setup() throws IOException, GeneralSecurityException {
        application = new Application();
    }

    @Test
    @Order(1)
    public void should_load_and_return_24_students_on_controller(){
        assertDoesNotThrow(() -> application.loadStudents());
        assertEquals(24, application.getStudentController().size());
    }

    @Test
    @Order(2)
    public void should_analyze_and_write_results_without_errors(){
        assertDoesNotThrow(() -> {
            application.analyzeUsers();
            application.writeResults();
        });
    }

    @Test
    @Order(3)
    public void should_clean_sheet_without_errors(){
        assertDoesNotThrow(() -> application.cleanSheet());
    }

}
