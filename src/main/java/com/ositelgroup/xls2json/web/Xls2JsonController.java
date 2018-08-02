package com.ositelgroup.xls2json.web;


import com.ositelgroup.xls2json.service.FileUploaderService;
import com.ositelgroup.xls2json.service.Xls2JsonService;
import com.ositelgroup.xls2json.service.XlsCellUpdaterService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class Xls2JsonController {


    @Autowired
    private FileUploaderService fileUploaderService;

    @Autowired
    private Xls2JsonService xls2JsonService;

    @Autowired
    private XlsCellUpdaterService xlsCellUpdaterService;

    @PutMapping("/ositel/addExcelFile")
    public Long addExcelFile(@RequestParam("pFileName") String pFileName) {
       return fileUploaderService.addXlsFile(pFileName).getId();
       //TODO create an object xlsFile and add it as attribute to the Session
       //The Id returned here will be used as {idExcelFile} in uploadExcelFile
    }



    @PostMapping("/ositel/{idExcelFile}/uploadExcelFile")
    public String uploadExcelFile (@PathVariable Long idExcelFile, MultipartFile pFile) {
        return fileUploaderService.uploadXlsFile(idExcelFile,pFile);
    }

    @PostMapping("/ositel/searchExcelFile")
    public String searchExcelFile(String pFilename) throws IOException {
        ArrayList<ArrayList<Object>> listOfCellValues = xls2JsonService.convert(pFilename);
        return xls2JsonService.toJson(listOfCellValues,pFilename);

    }


    @PostMapping("ositel/{pColonne}/{pLine}/updateCellValue")
    String updateCellValue(@PathVariable int pColonne,@PathVariable int pLine,
                           @RequestParam("pValue") String pValue,@RequestParam("pFileName") String pFileName) {

        return xlsCellUpdaterService.updateCell(pColonne,pLine,pValue,pFileName);

    }

}
