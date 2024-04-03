package me.finn.tofivem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class RPFSetup {

    public static void start(File rpfFile, File gtaUtilFolder, File filesFolder, File tempFolder, File scriptFolder, File streamFolder, File dataFolder, File manifestFile, Scanner scanner) throws IOException, InterruptedException {
        String fileName = rpfFile.getName();

        ProcessBuilder builder = new ProcessBuilder("powershell", "cd '" + gtaUtilFolder.getAbsolutePath() + "' ; " +
                "./gtautil extractarchive --input '" + rpfFile.getAbsolutePath() + "' --output '" + scriptFolder.getAbsolutePath() + "'");
        Process process = builder.start();
        process.waitFor();

        // Moving all files
        for (File allContent : tempFolder.listFiles()) {
            if (!allContent.isDirectory()) continue; // TODO: Check file type
            for (File unzippedFileContent : allContent.listFiles()) {
                if (!unzippedFileContent.isDirectory()) continue; // TODO: Check file type
                moveFile(streamFolder, dataFolder, unzippedFileContent);
            }
        }

        System.out.println("Done!" + System.lineSeparator() + System.lineSeparator());
    }

    private static void moveFile(File streamFolder, File dataFolder, File unzippedFileContent) throws IOException {
        for (File contentFile : unzippedFileContent.listFiles()) {
            if (!(contentFile.getName().toLowerCase().endsWith("meta") || contentFile.getName().toLowerCase().endsWith("ytd") ||
                    contentFile.getName().toLowerCase().endsWith("ydr") || contentFile.getName().toLowerCase().endsWith("yft") ||
                    contentFile.getName().toLowerCase().endsWith(".png") || contentFile.getName().toLowerCase().endsWith(".dds") ||
                    contentFile.getName().toLowerCase().endsWith(".bmp") || contentFile.getName().toLowerCase().endsWith(".jpg") ||
                    contentFile.getName().toLowerCase().endsWith(".jpeg"))) continue; // TODO: Handle

            if (contentFile.isDirectory()) moveFile(streamFolder, dataFolder, contentFile);

            if (contentFile.getName().toLowerCase().endsWith("meta")) {
                File targetFile = new File(dataFolder, contentFile.getName());
                File targetContentFile = FileManager.checkFile(targetFile, dataFolder, 1);

                System.out.println("Moving: " + contentFile.toPath());
                System.out.println("To: " + targetContentFile.toPath());

                Files.copy(contentFile.toPath(), targetContentFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println(System.lineSeparator() + System.lineSeparator() + "Done: " + targetContentFile.toPath());
            }

            if (contentFile.getName().toLowerCase().endsWith(".ytd") || contentFile.getName().toLowerCase().endsWith(".yft") ||
                    contentFile.getName().toLowerCase().endsWith(".ydr") || contentFile.getName().toLowerCase().endsWith(".png") ||
                    contentFile.getName().toLowerCase().endsWith(".dds") || contentFile.getName().toLowerCase().endsWith(".bmp") ||
                    contentFile.getName().toLowerCase().endsWith(".jpg") || contentFile.getName().toLowerCase().endsWith(".jpeg")) {

                File targetFile = new File(streamFolder, contentFile.getName());
                File targetContentFile = FileManager.checkFile(targetFile, streamFolder, 1);

                System.out.println("Moving: " + contentFile.toPath());
                System.out.println("To: " + targetContentFile.toPath());

                Files.copy(contentFile.toPath(), targetContentFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println(System.lineSeparator() + System.lineSeparator() + "Done: " + targetContentFile.toPath());
            }
        }
    }

}
