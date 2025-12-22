package restau.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileManagementService {

    private static final String BASE_PATH = "data/";

    public static <T> void writeListToFile(List<T> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("List is empty, nothing to write.");
            return;
        }

        String className = list.get(0).getClass().getSimpleName();
        String filePath = BASE_PATH + className + ".txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (T obj : list) {
                bw.write(obj.toString());
                bw.newLine();
            }
            System.out.println("Successfully wrote " + list.size() + " objects to file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

