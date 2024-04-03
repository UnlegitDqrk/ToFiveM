package me.finn.tofivem;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        File filesFolder = new File("files");
        File tempFolder = new File("temp");

        File gtaUtilFolder = new File("gtautil");
        File gtaUtilZip = new File("gtautil.zip");

        if (tempFolder.exists()) FileManager.deleteDirectoryRecursion(tempFolder);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scanner.close();
            if (tempFolder.exists()) FileManager.deleteDirectoryRecursion(tempFolder);
            if (gtaUtilFolder.exists()) FileManager.deleteDirectoryRecursion(gtaUtilFolder);
        }));

        filesFolder.mkdir();
        tempFolder.mkdir();
        gtaUtilFolder.mkdir();

        FileManager.downloadFile("https://github.com/indilo53/gtautil/releases/download/2.2.7/gtautil-2.2.7.zip", gtaUtilZip);
        FileManager.unzip(gtaUtilZip, gtaUtilFolder.getAbsolutePath());

        gtaUtilZip.delete();

        System.out.print("Please press 'ANY KEY' when you placed all files in 'files'-Folder!");
        scanner.nextLine();
        System.out.println(System.lineSeparator() + System.lineSeparator());

        for (File file : filesFolder.listFiles()) initFile(file, gtaUtilFolder, scanner, filesFolder, tempFolder);
    }

    public static void initFile(File file, File gtaUtilFolder, Scanner scanner, File filesFolder, File tempFolder) throws IOException, InterruptedException {
        System.out.println("Setup: " + file.getName());

        FileManager.deleteDirectoryRecursion(tempFolder);
        tempFolder.mkdir();

        if (file.isDirectory()) for (File dirFiles : filesFolder.listFiles()) initFile(dirFiles, gtaUtilFolder, scanner, filesFolder, tempFolder);

        File scriptFolder = new File("fivem_" + FileManager.getName(file.getName()).replace(" ", "-"));
        File streamFolder = new File(scriptFolder, "stream");
        File dataFolder = new File(scriptFolder, "data");
        File manifestFile = new File(scriptFolder, "fxmanifest.lua");

        if (!scriptFolder.exists()) scriptFolder.mkdir();
        if (!streamFolder.exists()) streamFolder.mkdir();
        if (!dataFolder.exists()) dataFolder.mkdir();
        if (!manifestFile.exists()) manifestFile.createNewFile();

        ManifestGenerator.setupManifestFile(manifestFile, scanner);
        System.out.println(System.lineSeparator());

        if (file.getName().endsWith("oiv")) OIVSetup.start(file, gtaUtilFolder, filesFolder, tempFolder, scriptFolder, streamFolder, dataFolder, manifestFile, scanner);
        else if (file.getName().endsWith("rpf")) RPFSetup.start(file, gtaUtilFolder, filesFolder, tempFolder, scriptFolder, streamFolder, dataFolder, manifestFile, scanner);
        else System.err.println("Unsupported file type!");
    }
}