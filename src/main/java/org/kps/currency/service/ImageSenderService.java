package org.kps.currency.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
public class ImageSenderService {

    @Value("${google.application_name}")
    private String APPLICATION_NAME;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${google.family_directory_id}")
    private String baseDirectoryId;
    private final Drive drive;

    public ImageSenderService(GoogleAuthorizationService authorizationService) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        drive = new Drive.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                authorizationService.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String sendImage(InputStream inputStream) throws IOException {
        File fileMetadata = new File();
    //todo разобраться с листом папок, если нет такой папки то создаем новую и кладем в мап,
        // если есть то можно закинуть в мапу что бы не дергать апи
        drive.files().get(baseDirectoryId).getClassInfo();
        var subFolder = createNewSubfolder();

        fileMetadata.setName(LocalDateTime.now() +".jpg");
        fileMetadata.setParents(Collections.singletonList(subFolder));

        AbstractInputStreamContent content = new InputStreamContent("image/jpg", inputStream);

        File uploadedFile = drive.files().create(fileMetadata, content)
                .setFields("id, name, parents")
                .execute();
        inputStream.close();
        return "File uploaded with ID: " + uploadedFile.getName();

    }

    private String createNewSubfolder() {
        LocalDate date = LocalDate.now();
        var subFolderName = date.getMonth() + " " + date.getYear();

        File fileMetadata = new File();
        fileMetadata.setName(subFolderName);
        fileMetadata.setParents(Collections.singletonList(baseDirectoryId));
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        File result;
        try {
             result = drive.files().create(fileMetadata)
                    .setFields("id, parents")
                    .execute();
        } catch (IOException e) {
            log.error("Error with creating directory", e);
            throw new RuntimeException(e);
        }
        return result.getId();
    }
}
