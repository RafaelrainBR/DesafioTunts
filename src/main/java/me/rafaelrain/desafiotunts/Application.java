package me.rafaelrain.desafiotunts;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.Getter;
import me.rafaelrain.desafiotunts.controller.StudentController;
import me.rafaelrain.desafiotunts.model.Student;
import me.rafaelrain.desafiotunts.service.SheetsService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class Application {
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final SheetsService sheetsService = new SheetsService();
    private final StudentController studentController;

    public Application() throws IOException, GeneralSecurityException {
        System.out.println("Initializing StudentController...");

        int totalDays = sheetsService.getTotalDays();
        this.studentController = new StudentController(totalDays);
    }

    public static void main(String[] args) {
        try {
            Application app = new Application();
            app.loadStudents();
            app.analyzeUsers();
            app.writeResults();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error has occurred when enabling the application. Stopping...");
        }
    }

    public void loadStudents() throws IOException {
        final long initialTime = System.currentTimeMillis();
        System.out.println("Loading students from Sheet...");

        for (List<Object> row : sheetsService.getStudentRows()) {
            Student student = Student.fromRow(row);
            studentController.add(student);
        }

        System.out.printf("Successfully loaded %s users in %dms.",
                studentController.size(),
                System.currentTimeMillis() - initialTime
        ).println();
    }

    public void analyzeUsers() {
        System.out.println("Analyzing all users...");

        studentController.analyzeAll();
    }

    public void writeResults() throws IOException {
        final long initialTime = System.currentTimeMillis();
        System.out.println("Saving users...");

        for (Student student : studentController.values()) {
            sheetsService.saveStudent(student);
        }

        long time = System.currentTimeMillis() - initialTime;
        System.out.printf("Successfully saved %s users in %s.",
                studentController.size(),
                time < 2500
                        ? time + "ms"
                        : TimeUnit.MILLISECONDS.toSeconds(time) + "s"
        ).println();
    }

    public void cleanSheet() throws IOException {
        sheetsService.cleanChanges();
    }

}
