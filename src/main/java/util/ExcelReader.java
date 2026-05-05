package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public static List<String> getEmails() {

        List<String> emails = new ArrayList<>();
        

        try {
            InputStream is = ExcelReader.class
                    .getClassLoader()
                    .getResourceAsStream("emails.xlsx");

            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell cell = row.getCell(0);
                if (cell == null) continue;

                String email = cell.getStringCellValue().trim();

                if (!email.isEmpty()) {
                    emails.add(email);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return emails;
    }
}