package me.rafaelrain.desafiotunts;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import me.rafaelrain.desafiotunts.credential.CredentialHelper;
import me.rafaelrain.desafiotunts.model.Aluno;
import me.rafaelrain.desafiotunts.worker.StudentWorker;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.rafaelrain.desafiotunts.info.Info.APPLICATION_NAME;
import static me.rafaelrain.desafiotunts.info.Info.JSON_FACTORY;

public class DesafioTunts {

    public DesafioTunts(String sheetId, String sheetName, String aulasRange, String alunosRange) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, CredentialHelper.getCredential(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        ValueRange response = service.spreadsheets().values()
                .get(sheetId, sheetName + "!" + aulasRange)
                .execute();

        final String valor = (String) response.getValues().get(0).get(0);
        final String numberOnly = valor.replaceAll("[^0-9]", "");
        final int aulas = Integer.parseInt(numberOnly);

        response = service.spreadsheets().values()
                .get(sheetId, sheetName + "!" + alunosRange)
                .execute();

        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("Não foi encontrado nenhum valor no alunosRange da planilha.");
            return;
        }

        StudentWorker controller = new StudentWorker(aulas);
        for (List<Object> row : values) {
            Aluno aluno = Aluno.fromRow(row);
            controller.add(aluno);
        }

        System.out.printf("Foram carregados %s alunos.%n", controller.size()).println();

        for (Aluno value : controller.values()) {
            ValueRange valueRange = new ValueRange()
                    .setValues(
                            Collections.singletonList(
                                    Arrays.asList(value.getSituacao(), value.getMedia())
                            )
                    );

            String range = String.format(
                    "%s!G%d:H%d",
                    sheetName,
                    value.getRowId(),
                    value.getRowId()
            );

            service.spreadsheets().values()
                    .update(sheetId, range, valueRange)
                    .setValueInputOption("RAW")
                    .execute();
        }
    }

    public static void main(String[] args) {
        final String sheetId = "1VM7VplX3aOJYOkxSu6V4iuAs1BkdHU-yH4jVQT1eHCo";

        final String sheetName = "engenharia_de_software";
        final String aulasRange = "A2:H2";
        final String alunosRange = "A4:F27";

        try {
            new DesafioTunts(sheetId, sheetName, aulasRange, alunosRange);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ocorreu um erro ao tentar iniciar. Terminando aplicação...");
        }
    }

}
