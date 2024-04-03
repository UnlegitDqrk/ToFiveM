package me.finn.tofivem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class OIVSetup {
    public static void start(File oivFile, File gtaUtilFolder, File filesFolder, File tempFolder, File scriptFolder, File streamFolder, File dataFolder, File manifestFile, Scanner scanner) throws IOException {
        String fileName = oivFile.getName();

        String newFileName = fileName + ".zip";
        File zipFile = new File(tempFolder, newFileName);

        if (!zipFile.exists()) zipFile.createNewFile();

        // Creating manifest file and renaming oiv file and unzipping
        Files.copy(oivFile.toPath(), zipFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        FileManager.unzip(zipFile, tempFolder.getAbsolutePath());

        // Moving all files
        for (File allContent : tempFolder.listFiles()) {
            if (!allContent.isDirectory()) continue; // TODO: Check file type
            for (File unzippedFileContent : allContent.listFiles()) {
                if (!unzippedFileContent.isDirectory()) continue; // TODO: Check file type
                moveFile(streamFolder, unzippedFileContent);
            }
        }

        System.out.println("Done!" + System.lineSeparator() + System.lineSeparator());
    }

    private static void moveFile(File streamFolder, File unzippedFileContent) throws IOException {
        for (File contentFile : unzippedFileContent.listFiles()) {
            if (!(contentFile.getName().toLowerCase().endsWith(".ytd") || contentFile.getName().toLowerCase().endsWith(".yft") ||
                    contentFile.getName().toLowerCase().endsWith(".ydr") || contentFile.getName().toLowerCase().endsWith(".png") ||
                    contentFile.getName().toLowerCase().endsWith(".dds") || contentFile.getName().toLowerCase().endsWith(".bmp") ||
                    contentFile.getName().toLowerCase().endsWith(".jpg") || contentFile.getName().toLowerCase().endsWith(".jpeg"))) continue; // TODO: Handle

            if (contentFile.isDirectory()) moveFile(streamFolder, contentFile);

            File targetFile = new File(streamFolder, contentFile.getName());
            File targetContentFile = FileManager.checkFile(targetFile, streamFolder, 1);

            System.out.println("Moving: " + contentFile.toPath());
            System.out.println("To: " + targetContentFile.toPath());

            Files.copy(contentFile.toPath(), targetContentFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println(System.lineSeparator() + System.lineSeparator() + "Done: " + targetContentFile.toPath());
        }
    }
}
