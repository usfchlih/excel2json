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


/**
 * Service for uploading files
 */
@Service
public class FileUploaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploaderService.class);

    private static final Path STORE_LOCATION = Paths.get("upload_repo");


    @Autowired
    private ExcelFileRepository excelFileRepository;


    // add an Excel file , choose a name

    /**
     * This method will create an object ExcelFile and store it in an h2 database
     * to update later on when the actual file will uploaded
     *
     * @param pFileName fileName chosen by the client
     * @return
     */
    public ExcelFile addXlsFile(String pFileName){

        ExcelFile xlsFile = new ExcelFile(pFileName, false);
        return excelFileRepository.save(xlsFile);
    }



    /**
     * Upload an Excel file
     * @param pId database auto generated Id
     * @param pFile
     * @return Filename of the uploaded Excel file.
     */
    public String uploadXlsFile(Long pId, MultipartFile pFile) {

        if((pFile!=null)&&(pFile.isEmpty())){
            LOGGER.error(" File couldn't uploaded " ,pId);
            return "{}";
        }
        Optional<ExcelFile> fileHolder = excelFileRepository.findById(pId);
        if(!fileHolder.isPresent()) {
            LOGGER.error("File with id = {} hasn't been added yet, " +
                    "Please use the WS /ositel/addExcelFile to add it",pId);
            //return an empty String
            return "{}";
        }
        ExcelFile xlsFile = fileHolder.get();

        if(xlsFile != null) {
            if(xlsFile.isUploaded()){
                // a behavior here should be specified
                LOGGER.warn("This file {} has already been uploaded, The stored file will be overwritten",
                        xlsFile.getFileName());
            }
        }else {
            LOGGER.error("File hasn't been added yet, " +
                    "Please use the WS /ositel/addExcelFile to add it");
            //return an empty String
            return "{}";
        }


        String filename = StringUtils.cleanPath(pFile.getOriginalFilename());

        if (filename.contains("..")) {
            // This is a security check
            LOGGER.error("Cannot store file with relative path outside current directory : {}", filename);
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
                    xlsFile.setPath(STORE_LOCATION.toString() + File.separator + xlsFile.getFileName());

            // update stored XlsFile
            excelFileRepository.save(xlsFile);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        return xlsFile.getFileName();
    }

}
