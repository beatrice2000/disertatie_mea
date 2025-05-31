package com.beatrice.book.file;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @NonNull MultipartFile sourceFile,
            @NonNull String userId) {
        final String fileUploadSubPath = "users" + separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(
             @NonNull MultipartFile sourceFile,
             @NonNull String fileUploadSubPath) {
        // aici compun path-ul final pentru poza
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        // facem o verificare daca avem deja fisierul targetFolder
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Could not create folder " + targetFolder.getAbsolutePath());
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        // locatia unde vrem sa salvam filePath-ul si punem si milisecundele => o sa fie ceva de genul: ./upload/users/1/232324323333.jpg
        String targetFilePath = finalUploadPath + separator + currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        // fac si un try and catch pt ca atunci ca scriu numele fiserului s-ar putea sa obtin o execeptie input output
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("Successfully uploaded " + sourceFile.getOriginalFilename() + " to " + targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("Failed to upload " + sourceFile.getOriginalFilename() + " to " + targetFilePath, e);
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        // vrem sa obtinem extensia, ce avem dupa .
        int lastDotIndex = fileName.lastIndexOf(".");
        // facem o verificare si pentru cazul in care fiserul pe care il incarcam nu are o extensie
        if (lastDotIndex == -1) {
            return "";
        }
        //  vrem sa returnam extensia fiserului trasnformata in litere mici
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

}
