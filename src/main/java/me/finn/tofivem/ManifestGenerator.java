package me.finn.tofivem;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class ManifestGenerator {

    private static String generateManifestFile(String author, String version, String description, boolean useLua54) {
        return "-- Generated with ToFiveM by PhoenixV" + System.lineSeparator() +
                "fx_version 'cerulean'" + System.lineSeparator() +
                (useLua54 ? "lua54 'yes'" + System.lineSeparator() : "") +
                "game 'gta5'" + System.lineSeparator() + System.lineSeparator() +
                "author 'Original author: " + author + " | Converted by ToFiveM by PhoenixV' -- Do not remove/change this" + System.lineSeparator() +
                "description '" + description + "'" + System.lineSeparator() +
                "version '" + version + "'";
    }

    public static void setupManifestFile(File manifestFile, Scanner scanner) throws IOException {
        System.out.print("Author of original file (Default: Unknown): ");
        String author = scanner.nextLine();

        System.out.print("Version of original file (Default: 1.0.0): ");
        String version = scanner.nextLine();

        System.out.print("Description of original file (Default: Converted by ToFiveM by PhoenixV): ");
        String description = scanner.nextLine();

        if (author.isBlank() || author.isEmpty()) author = "Unknown";
        if (version.isBlank() || version.isEmpty()) version = "1.0.0";
        if (description.isBlank() || description.isEmpty()) description = "Converted by ToFiveM by PhoenixV";

        Files.writeString(manifestFile.toPath(), generateManifestFile(author, version, description, setupLua54(scanner)), StandardCharsets.UTF_8, StandardOpenOption.WRITE);
    }

    private static boolean setupLua54(Scanner scanner) {
        System.out.print("Use lua54 (y/N): ");
        String answer = scanner.nextLine();

        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("true") || answer.equalsIgnoreCase("1") ||
                answer.equalsIgnoreCase("y")) return true;
        else if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("false") || answer.equalsIgnoreCase("0") ||
                answer.equalsIgnoreCase("n")) return false;
        else {
            System.out.println("Wrong answer!");
            return setupLua54(scanner);
        }
    }

}
