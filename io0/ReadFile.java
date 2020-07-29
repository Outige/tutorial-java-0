import java.io.*;
import java.util.*;

public class ReadFile {
    private Scanner x;

    public int openFile(String f) {
        try {
            x = new Scanner(new File(f));
        } catch (Exception e) {
            System.out.println("could not open file");
            return 1;
        }
        return 0;
    }

    public void readFile() {
        String line;
        while (x.hasNextLine()) {
            line = x.nextLine();
            System.out.println(line);
        }
    }

    public int closeFile() {
        try {
            x.close();
        } catch (Exception e) {
            System.out.println("could not close file");
            return 1;
        }
        return 0;
    }
}