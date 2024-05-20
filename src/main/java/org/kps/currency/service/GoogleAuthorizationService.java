package org.kps.currency.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Service
public class GoogleAuthorizationService {

    @Value("${google.service_key_file_path}")
    private String serviceKeyPath;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public Credential getCredentials(final NetHttpTransport httpTransport)
            throws IOException {

        InputStream credentialsStream = getClass().getResourceAsStream(serviceKeyPath);
        if (credentialsStream == null) {
            throw new FileNotFoundException("Resource not found: service_key.json");
        }
        return GoogleCredential.fromStream(credentialsStream, httpTransport, JSON_FACTORY)
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/drive"));

    }
}
