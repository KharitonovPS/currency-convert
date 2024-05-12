package org.kps.currency.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class ImageSenderService {

    @Value("${google.application_name}")
    private String APPLICATION_NAME;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${google.family_directory_id}")
    private String baseDirectoryId;
    private final Drive drive;
    private static final HashMap<String, String> SUBFOLDERS_IDS_MAP = new HashMap<>();

    @PostConstruct
    void init() {
        try {
            log.info("Start to initialize SUBFOLDERS_IDS_MAP...");
            FileList execute = drive.files().list().setQ(
                    "mimeType = 'application/vnd.google-apps.folder' and trashed = false"
            ).execute();
            List<File> files = execute.getFiles();
            files.forEach(f -> SUBFOLDERS_IDS_MAP.put(f.getName(), f.getId()));
            log.info("Complete successfully {}", SUBFOLDERS_IDS_MAP);
        } catch (IOException e) {
            log.error("Can`t initialize subfoldersIdsMap", e);
            throw new RuntimeException(e);
        }
    }

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
        var subfolderName = generateSubfolderName();
        String subfolderId = subfolderChecks(subfolderName);

        File fileMetadata = new File();
        fileMetadata.setName(LocalDateTime.now() + ".jpg");
        fileMetadata.setParents(Collections.singletonList(subfolderId));

        AbstractInputStreamContent content = new InputStreamContent("image/jpg", inputStream);

        File uploadedFile = drive.files().create(fileMetadata, content)
                .setFields("id, name, parents")
                .execute();
        inputStream.close();
        return "File uploaded with ID: " + uploadedFile.getName();

    }

    private String subfolderChecks(String subfolderName) {
        String subfolderId;
        if (SUBFOLDERS_IDS_MAP.containsKey(subfolderName)) {
            subfolderId = SUBFOLDERS_IDS_MAP.get(subfolderName);
        } else {
            log.warn("Can not find subFolder={}, create new one", subfolderName);
            subfolderId = createNewSubfolder(subfolderName);
            SUBFOLDERS_IDS_MAP.put(subfolderName, subfolderId);
        }
        return subfolderId;
    }

    private String createNewSubfolder(String subFolderName) {
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

    private static String generateSubfolderName() {
        LocalDate date = LocalDate.now();
        var subFolderName = date.getMonth() + " " + date.getYear();
        return subFolderName;
    }
}
