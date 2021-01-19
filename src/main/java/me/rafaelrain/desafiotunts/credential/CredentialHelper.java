package me.rafaelrain.desafiotunts.credential;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.*;
import java.util.Collections;

import static me.rafaelrain.desafiotunts.Application.JSON_FACTORY;

public class CredentialHelper {

    private static final String CREDENTIALS_PATH = "credentials.json";
    private static final String TOKENS_DIR_PATH = "tokens";

    public static Credential getCredential(NetHttpTransport transport) throws IOException {
        final InputStream in = CredentialHelper.class.getClassLoader().getResourceAsStream(CREDENTIALS_PATH);
        if(in == null)
            throw new FileNotFoundException("File not found: " + CREDENTIALS_PATH);

        final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                transport, JSON_FACTORY, clientSecrets, Collections.singletonList(SheetsScopes.SPREADSHEETS)
        )
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIR_PATH)))
                .setAccessType("offline")
                .build();

        final LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
