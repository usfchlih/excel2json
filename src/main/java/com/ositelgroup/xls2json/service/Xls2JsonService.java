package com.ositelgroup.xls2json.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.ositelgroup.xls2json.model.ExcelFileRepository;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Xls2JsonService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploaderService.class);

    private static final Path STORE_LOCATION = Paths.get("upload_repo");

    @Autowired
    private ExcelFileRepository excelFileRepository;


    public Xls2JsonService() {

    }


    public ArrayList<ArrayList<Object>> convert(String pFileName) throws IOException {

        //get filename from database
        String filepath = STORE_LOCATION.toString() + File.separator + excelFileRepository.findByFileName(pFileName).getFileName();
        InputStream InputStream = new FileInputStream(new File(filepath));
        XSSFWorkbook workBook = new XSSFWorkbook(InputStream);
        XSSFSheet sheet = workBook.getSheetAt(0);

        ArrayList<ArrayList<Object>> listCellValues = new ArrayList<>(sheet.getLastRowNum() + 1);
        for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum() + 1; j++) {
            Row row = sheet.getRow(j);
            if (row == null) {
                continue;
            }
            boolean hasValues = false;
            ArrayList<Object> rowData = new ArrayList<Object>();
            for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
                Cell cell = row.getCell(k);
                if (cell != null) {
                    Object value = cellToObject(cell);
                    hasValues = hasValues || value != null;
                    rowData.add(value);
                } else {
                    rowData.add(null);
                }
            }

            listCellValues.add(rowData);
        }
        return listCellValues;

    }

    private Object cellToObject(Cell cell) {

        int type = cell.getCellType();

        if (type == Cell.CELL_TYPE_STRING) {
            return cleanString(cell.getStringCellValue());
        }

        if (type == Cell.CELL_TYPE_BOOLEAN) {
            return cell.getBooleanCellValue();
        }

        if (type == Cell.CELL_TYPE_NUMERIC) {

            if (cell.getCellStyle().getDataFormatString().contains("%")) {
                return cell.getNumericCellValue() * 100;
            }

            return numeric(cell);
        }

        if (type == Cell.CELL_TYPE_FORMULA) {
            switch (cell.getCachedFormulaResultType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    return numeric(cell);
                case Cell.CELL_TYPE_STRING:
                    return cleanString(cell.getRichStringCellValue().toString());
            }
        }

        return null;

    }

    private String cleanString(String pStr) {
        return pStr.replace("\n", "").replace("\r", "");
    }

    private Object numeric(Cell pCell) {
        if (HSSFDateUtil.isCellDateFormatted(pCell)) {

            return SimpleDateFormat.getInstance().format(pCell.getDateCellValue());
        }
        return Double.valueOf(pCell.getNumericCellValue());
    }

    public String toJson(ArrayList<ArrayList<Object>> pListOfCellValues, String pFileName) {

        StringBuilder jsonStr = new StringBuilder();
        jsonStr.append("{\"fileName\":" + "\"" + pFileName + "\"");
        jsonStr.append(" \"headerColumn\":[ ");
        int numberOfCols =  pListOfCellValues.get(0).size();
        for (int i = 0; i < numberOfCols; i++) {
            jsonStr.append("\"" + pListOfCellValues.get(0).get(i) + "\"");
            if (i < pListOfCellValues.get(0).size() - 1) {
                jsonStr.append(", ");
            } else {
                jsonStr.append(" ]");
            }
        }
        jsonStr.append("\"linesValue\":{");
        for (int i = 1; i < pListOfCellValues.size() ;i++) {
            jsonStr.append("\"" + i + "\": [");
            for (int j = 0; j < pListOfCellValues.get(i).size();j++) {
                jsonStr.append("\"" + pListOfCellValues.get(i).get(j) + "\"");
                if (j < pListOfCellValues.get(i).size() - 1) {
                    jsonStr.append(", ");
                } else {
                    jsonStr.append(" ]");
                }
            }
            if (i < pListOfCellValues.size() - 1) {
                jsonStr.append(", ");
            } else {
                jsonStr.append("}");
            }
        }
        jsonStr.append("}");

        return jsonStr.toString();
    }

}