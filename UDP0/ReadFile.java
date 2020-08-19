import java.io.*;
import java.util.*;

import java.util.logging.Level; 
import java.util.logging.Logger;
import java.lang.Math;


public class ReadFile {
    public Logger logger = Logger.getLogger(ReadFile.class.getName());

    private Scanner x;
    private String path;

    ReadFile(String path, Level l) {
        logger.setLevel(l);
        this.path = path;
    }

    public int openFile() {
        try {
            x = new Scanner(new File(this.path));
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    // public void read(String[] arr) {
    //     String line;
    //     int count = 0;
    //     while (x.hasNextLine()) {
    //         line = x.nextLine();
    //         arr[count] = line;
    //         count++;
    //     }
    // }

    public String readFile() {
        String out;

        out = "";
        if (openFile() == 1) {
            logger.log(Level.WARNING, "error opening file " + path);
        }

        /* file to string */
        while (x.hasNextLine()) {
            out += x.nextLine();
            if (x.hasNextLine()) {
                out += "\n";
            }
        }

        if (closeFile() == 1) {
            logger.log(Level.WARNING, "error closing file " + path);
        }
        return out;
    }

    public String[] fileArray(int size) {
        String[] fileArray;
        int count;

        String file = readFile();

        count = (int) Math.ceil((double)file.length()/size);
        fileArray = new String[count];

        count = 0;
        for (int i = 0; i < file.length(); i+=size) {
            int len;
            if (i+size <= file.length()) {
                len = size;
            } else {
                len = file.length() - i;
            }
            fileArray[count++] = file.substring(i, i+len);
            logger.log(Level.INFO, String.format("frame: %d/%d\n%s~EOF\n%d\n", count-1, fileArray.length-1, fileArray[count-1], fileArray[count-1].length()));
        }

        return fileArray;
    }

    public int closeFile() {
        try {
            x.close();
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    public int fileSize(String f) {
        String line;
        int count = 0;

        /* try open file */
        try {
            x = new Scanner(new File(f));
        } catch (Exception e) {
            return 0;
        }

        /* count lines in file */
        while (x.hasNextLine()) {
            count++;
            line = x.nextLine();
        }

        /* close file */
        try {
            x.close();
        } catch (Exception e) {
            return 0;
        }
        return count;
    }
}