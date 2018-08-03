# excel2json

##### To Run : ./mvnc spring-boot:run

#### Testing with Httpie : [a link]httpie.org

### Step 1 :
##### Add Excel file to be uploaded.
http PUT :8080/api/ositel/addExcelFile pFileName==file1.xlsx

### Step 2 :
##### Upload the file real_file.xlsx and store it as file1.xlsx.
http --form POST :8080/api/ositel/1/uploadExcelFile pFile@"/path/real_file.xlsx"

### Step 3 : 
##### Search for file with name file1.xlsx and return its content as json.
http POST :8080/api/ositel/searchExcelFile pFilename==file1.xlsx

### Step 4 :
##### Update the Cell in col:2, line:3 with the value: "newVal" 
http POST :8080/api/ositel/2/3/updateCellValue pValue=="newVal" pFileName==file1.xlsx

### Step 5 :
##### Run searchExcelFile to verify that the previous change took place.
http POST :8080/api/ositel/searchExcelFile pFilename==file1.xls
