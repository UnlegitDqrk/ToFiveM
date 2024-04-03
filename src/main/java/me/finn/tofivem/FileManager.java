package me.finn.tofivem;

import java.io.*;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileManager {


    public static void deleteDirectoryRecursion(File file) {
        if (file.exists() && file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) for (File entry : entries) deleteDirectoryRecursion(entry);
        }

        if (!file.exists()) {
            System.err.println("File '" + file.toPath() + "' does not exists!");
            return;
        }

        if (!file.delete()) System.err.println("Failed to delete '" + file.toPath() + "'!");
    }

    public static String getName(String fileName) {
        String[] splitName = fileName.split("\\.");
        return splitName[splitName.length - 2];
    }

    public static void unzip(File source, String outputDirectory) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {
            ZipEntry entry = zis.getNextEntry();

            while (entry != null) {
                File file = new File(outputDirectory, entry.getName());

                if (entry.isDirectory()) file.mkdirs();
                else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) parent.mkdirs();

                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                        int bufferSize = Math.toIntExact(entry.getSize());
                        byte[] buffer = new byte[bufferSize > 0 ? bufferSize : 1];
                        int location;

                        while ((location = zis.read(buffer)) != -1) bos.write(buffer, 0, location);
                    }
                }

                entry = zis.getNextEntry();
            }
        }
    }

    public static File checkFile(File file, File folder, int stage) {
        if (!file.exists()) return file;

        file = new File(folder, stage + file.getName());
        int newStage = stage + 1;

        if (!file.exists()) return file;
        else return checkFile(file, folder, newStage);
    }

    public static void downloadFile(String urlStr, File outputFile) throws IOException {
        URL url = new URL(urlStr);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

        byte[] buffer = new byte[1024];
        int count=0;

        while((count = bufferedInputStream.read(buffer,0,1024)) != -1) fileOutputStream.write(buffer, 0, count);

        fileOutputStream.close();
        bufferedInputStream.close();
    }
}
