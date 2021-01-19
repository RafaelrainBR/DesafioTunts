package me.rafaelrain.desafiotunts.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.val;
import me.rafaelrain.desafiotunts.credential.CredentialHelper;
import me.rafaelrain.desafiotunts.model.Student;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.rafaelrain.desafiotunts.Application.JSON_FACTORY;

public class SheetsService {

    public static final String APPLICATION_NAME = "desafiotunts";
    private static final String SHEET_ID = "1VM7VplX3aOJYOkxSu6V4iuAs1BkdHU-yH4jVQT1eHCo";

    private static final String SHEET_NAME = "engenharia_de_software";
    private static final String TOTAL_DAYS_RANGE = "A2:H2";
    private static final String STUDENTS_RANGE = "A4:F27";

    private final Sheets service;

    public SheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        this.service = new Sheets.Builder(transport, JSON_FACTORY, CredentialHelper.getCredential(transport))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public int getTotalDays() throws IOException {
        val values = getValues(TOTAL_DAYS_RANGE);
        final String result = (String) values.get(0).get(0);

        final String afterRegex = result.replaceAll("[^0-9]", "");
        return Integer.parseInt(afterRegex);
    }

    public List<List<Object>> getStudentRows() throws IOException {
        return getValues(STUDENTS_RANGE);
    }

    public void saveStudent(Student student) throws IOException {
        final int row = student.getRowId();
        final String range = String.format("G%d:H%d", row, row);

        updateRange(range,
                Collections.singletonList(
                        Arrays.asList(student.getSituation(), student.getRemainGrade())
                )
        );
    }

    public void cleanChanges() throws IOException {
        String range = "G4:H27";

        updateRange(range,
                Collections.nCopies(24,
                        Arrays.asList("", "")
                )
        );
    }

    private List<List<Object>> getValues(String range) throws IOException {
        final ValueRange response = service.spreadsheets().values()
                .get(SHEET_ID, buildRange(range))
                .execute();

        return response.getValues();
    }

    private void updateRange(String range, List<List<Object>> rows) throws IOException {
        final ValueRange valueRange = new ValueRange()
                .setValues(rows);

        service.spreadsheets().values()
                .update(SHEET_ID, buildRange(range), valueRange)
                .setValueInputOption("RAW")
                .execute();
    }

    private String buildRange(String range) {
        return SHEET_NAME + "!" + range;
    }
}
