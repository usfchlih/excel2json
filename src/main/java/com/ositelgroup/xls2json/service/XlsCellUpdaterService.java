package com.ositelgroup.xls2json.service;


import com.ositelgroup.xls2json.model.ExcelFile;
import com.ositelgroup.xls2json.model.ExcelFileRepository;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Service
public class XlsCellUpdaterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploaderService.class);


    @Autowired
    private ExcelFileRepository excelFileRepository;

    public String updateCell(int pCol, int pLine, String pValue, String pFileName) {
            String str = "pCol : "+ pCol +", pLine: "+ pLine +", pValue: "+ pValue+", pFileName : "+pFileName;
        try {

            String filepath = null;
            //search for ExcelFile in the database
            ExcelFile xlsFile = excelFileRepository.findByFileName(pFileName);

            if(xlsFile != null) {
                filepath = xlsFile.getPath();
                if(filepath == null) {
                    LOGGER.error("File added but not uploaded," +
                            "Please use the WS /ositel/{idExcelFile}/uploadFile to upload your file");
                    //return an empty String
                    return "{}";


                }
            } else {
                LOGGER.error("Please use the following WS /ositel/addExcelFile to add your file");
                return "{}";
            }

            InputStream InputStream = new FileInputStream(new File(filepath));
            XSSFWorkbook workBook = new XSSFWorkbook(InputStream);


            XSSFSheet sheet = workBook.getSheetAt(0);

            Cell cell = null;

            //Retrieve the row and check for null
            //Row index start at 0, but the first Row is not included (Header)
            XSSFRow sheetrow = sheet.getRow(pLine);
            if (sheetrow == null) {
                sheetrow = sheet.createRow(pLine);
            }

            //Update the value of cell
            //Row index starts at 0 ==> pCol - 1
            cell = sheetrow.getCell(pCol - 1);
            if (cell == null) {
                cell = sheetrow.createCell(pCol - 1);
            }
            cell.setCellValue(pValue);

            InputStream.close();

            FileOutputStream outFile = new FileOutputStream(new File(filepath));
            workBook.write(outFile);
            outFile.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return str;
    }
}
