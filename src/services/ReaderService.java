package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReaderService {
    private ReaderService() {}
    private static ReaderService instance = null;

    public static ReaderService getInstance() {
        if(instance == null) {
            instance = new ReaderService();
        }
        return instance;
    }

    public ArrayList<String> readFromCSV(String inputFile) {
        ArrayList<String> lines = new ArrayList<>();
        String line;

        try (var in = new BufferedReader(new FileReader(inputFile))) {
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return lines;
    }
}
