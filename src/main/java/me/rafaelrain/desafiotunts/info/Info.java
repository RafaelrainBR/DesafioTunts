package me.rafaelrain.desafiotunts.info;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class Info {

    public static final String APPLICATION_NAME = "desafiotunts";
    public static final String CREDENTIALS_PATH = "credentials.json";
    public static final String TOKENS_DIR_PATH = "tokens";

    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static final String COLUNA_SITUACAO = "G";
    public static final String COLUNA_NOTA_APROVACAO = "H";

}
