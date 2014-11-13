package io.github.lucariatias.amethyst.common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileUtils {

    private FileUtils() {}

    public static boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] fileList = directory.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    deleteDirectory(file);
                }
            }
        }
        return directory.delete();
    }

    public static Map<String, Object> loadMetadata(File file) throws IOException {
        StringBuilder metadataBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            metadataBuilder.append(line).append('\n');
        }
        reader.close();
        Gson gson = new Gson();
        return gson.fromJson(metadataBuilder.toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
    }

    public static void saveMetadata(Map<String, Object> metadata, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        Gson gson = new Gson();
        writer.write(gson.toJson(metadata));
        writer.close();
    }

    public static String read(File file) throws FileNotFoundException {
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(new FileInputStream(file));
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine()).append('\n');
        }
        return builder.toString();
    }

}
