package org.kps.currency.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ImageSenderService {
    private final GoogleAuthorizationService authorizationService;

    @Value("${google.application_name}")
    private String APPLICATION_NAME;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final Drive drive;

    public ImageSenderService(GoogleAuthorizationService authorizationService) throws GeneralSecurityException, IOException {
        this.authorizationService = authorizationService;
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        drive = new Drive.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                authorizationService.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String sendImage(java.io.File file) throws IOException, GeneralSecurityException {
        com.google.api.services.drive.model.File fileMetadata = new File();
        fileMetadata.setName(LocalDateTime.now().toString());


        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType == null) {
            mimeType = "image/jpg";
        }
        FileContent mediaContent = new FileContent(mimeType, file);
        File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                .setFields("id, name")
                .execute();

        return "File uploaded with ID: " + uploadedFile.getName();
    }


}
