package com.homepainter.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ReadCsv {

    /**
     * 读取csv
     * @param csvFile
     * @return
     */
    public static List<String[]> readCsvFile(String csvFile) {
        List<String[]> csvData = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                csvData.add(line);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return csvData;
    }


    /**
     * 读取csv
     * @param filepath
     * @return
     * @throws IOException
     */
    public static List<String[]>  readCsv(String filepath) throws IOException {
        // 获取文件路径
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();


        Resource resource = null;
        InputStream stream = null;

        System.out.println(filepath);
        resource = resolver.getResource(filepath);
        stream = resource.getInputStream();

        List<String[]> res = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(stream))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // 处理读取到的一行数据
                // nextLine是一个String数组，包含了这一行中的所有列
                res.add(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return res;
    }
}



