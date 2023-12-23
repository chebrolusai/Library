package com.oodProject.library.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvFileUtil {
	
	public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
            	System.out.println(line);
                String[] lineData = line.split(",");
                data.add(lineData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
	
	
	public static void writeCSV(String filePath, List<String[]> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            for (String[] lineData : data) {
                bw.write(String.join(",", lineData));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
