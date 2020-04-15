package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileManager {
    public static BufferedReader input;

    static {
        try {
            input = new BufferedReader(new FileReader(Constants.getInputFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
