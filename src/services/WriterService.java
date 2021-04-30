package services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class WriterService {
    private WriterService() {}
    private static WriterService instance = null;

    public static WriterService getInstance() {
        if(instance == null) {
            instance = new WriterService();
        }
        return instance;
    }

    public void audit(String methodName) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        try(var out = new BufferedWriter(new FileWriter("audit.csv", true))) {
            String message = "Method " + methodName + " was called," + timestamp.toString() + "\n";
            out.write(message);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
