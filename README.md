# excel2json

## Add Excel file to be uploaded.
http PUT :8080/api/ositel/addExcelFile pFileName==file1.xls

## Upload the file real_file.xlsx and store it as file1.xls.
http --form POST :8080/api/ositel/1/uploadExcelFile pFile@"/path/real_file.xlsx"

## Search for file with name file1.xls and return its content as json.
http POST :8080/api/ositel/searchExcelFile pFilename==file1.xls

## Update the Cell in col:2, line:3 with the value: "newVal" 
http POST :8080/api/ositel/2/3/updateCellValue pValue=="newVal" pFileName==file1.xls

## Run this to verify the change in col:2,line:3 took place
http POST :8080/api/ositel/searchExcelFile pFilename==file1.xls
