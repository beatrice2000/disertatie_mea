package com.beatrice.book.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Slf4j
public class FileUtils {
    public static byte[] readFileFromLocation(String fileUrl) {
        // daca nu avem nimic sau daca nu am primit niciun fiser URL
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }
        // altfel folosim un try and catch
        try {
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.warn("File {} not found", fileUrl);
        }
        return null;
    }

}
