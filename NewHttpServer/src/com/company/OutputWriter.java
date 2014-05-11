package com.company;

import java.io.*;
import java.util.List;


public class OutputWriter {

    public void write(List<String> resMessage, OutputStream output)  {

        for (String s : resMessage) {
            if (s != null) {
                try {
                    output.write(s.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(FileInputStream inFile, OutputStream output) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inFile));
        PrintWriter out = new PrintWriter(output, true);
        String line;

        try {
            while ((line = bufferedReader.readLine()) != null) {

                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            try {
                inFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
