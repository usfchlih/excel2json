package com.ositelgroup.xls2json.service;


import com.ositelgroup.xls2json.model.ExcelFile;
import com.ositelgroup.xls2json.model.ExcelFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;


@Service
public class FileUploaderService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploaderService.class);

    private static final Path STORE_LOCATION = Paths.get("upload_repo");

    @Autowired
    private ExcelFileRepository excelFileRepository;


    // add an Excel file , choose a name
    public ExcelFile addXlsFile(String pFileName){
        ExcelFile xlsFile = new ExcelFile(pFileName, false);
        return excelFileRepository.save(xlsFile);
    }


    // Upload an excel file
    public String uploadXlsFile(Long pId, MultipartFile pFile) {

        Optional<ExcelFile> fileHolder = excelFileRepository.findById(pId);
        ExcelFile xlsFile = fileHolder.get();


        String filename = StringUtils.cleanPath(pFile.getOriginalFilename());

        logger.info("Root path : {}", STORE_LOCATION.getRoot());

        if (filename.contains("..")) {
            // This is a security check
            logger.error("Cannot store file with relative path outside current directory : {}", filename);
        }
        File directory = new File(STORE_LOCATION.toString());
        try (InputStream inputStream = pFile.getInputStream()) {

            //Files.create(storeLocation);
            if(!directory.exists()) {
                Files.createDirectory(directory.toPath());
            }

            // Copy uploaded file to the file repo : storeLocation
            Files.copy(inputStream, this.STORE_LOCATION.resolve(xlsFile.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);

                    xlsFile.setUploaded(true);


            // update stored XlsFile
            excelFileRepository.save(xlsFile);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        return xlsFile.getFileName();
    }

}
