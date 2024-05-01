package org.kps.currency.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
public class ImageSenderService {

    private final GoogleAuthorizationService authorizationService;

    @Value("${google.application_name}")
    private String APPLICATION_NAME;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();


    public String sendImage() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                authorizationService.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return null;
    }
}
