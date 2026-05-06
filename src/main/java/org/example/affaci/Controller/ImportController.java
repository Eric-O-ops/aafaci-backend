package org.example.affaci.Controller;


import lombok.RequiredArgsConstructor;
import org.example.affaci.Service.CsvImportService;
import org.example.affaci.Service.ExcelImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final ExcelImportService excelImportService;
    private final CsvImportService csvImportService;




    @PostMapping(path = "/Old_file", consumes = "multipart/form-data")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        try{
            excelImportService.importExcel(file);
            return ResponseEntity.ok("Import successful");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Import failed" + e.getMessage());
        }
    }


    @PostMapping(
            value = "/national",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveVersion2(
            @RequestPart("file") MultipartFile file,
            @RequestPart("photos") MultipartFile photosZip) {
        if(file.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Файл не должен быть пустым");
        }

        String filename = file.getOriginalFilename();
        String photoFile = photosZip.getOriginalFilename();
        if(filename == null || !filename.toLowerCase().endsWith(".xlsx") || !photoFile.toLowerCase().endsWith(".zip") || photoFile == null){
            return ResponseEntity
                    .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        try {
            excelImportService.importExcelNationalFood(file, photosZip);
            return ResponseEntity
                    .ok("Импорт успешно выполнен для файла: " + filename + " , " + photoFile);
        } catch (IllegalArgumentException iae) {
            // например, минерал или категория не найдены
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка данных: " + iae.getMessage());
        } catch (Exception e) {
            // общая ошибка сервера
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при импорте: " + e.getMessage());
        }
    }


    @PostMapping(value = "/standart", consumes = "multipart/form-data")
    public ResponseEntity<String> saveStandart(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Файл не должен быть пустым");
        }
        String filename = file.getOriginalFilename();
        if(filename == null || !filename.toLowerCase().endsWith(".xlsx")){
            return ResponseEntity
                    .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Только .xlsx файлы поддерживаются");
        }

        try{
            excelImportService.importExcelFinal(file);
            return ResponseEntity.ok("Импорт успешно выполнен для файла: " + filename);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка данных: " + iae.getMessage());
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при импорте: " + e.getMessage());
        }
    }

    @PostMapping(
            value = "/excel-eng-version",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importEngVersion(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Файл не должен быть пустым");
        }
        String filename = file.getOriginalFilename();
        if(filename == null || !filename.toLowerCase().endsWith(".xlsx")){
            return ResponseEntity
                    .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Только .xlsx файлы поддерживаются");
        }

        try{
            excelImportService.importExcelOld_English(file);
            return ResponseEntity.ok("Импорт успешно выполнен для файла: " + filename);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка данных: " + iae.getMessage());
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при импорте: " + e.getMessage());
        }
    }



    @PostMapping(
            value = "/update_DB_checkin",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> checkDB(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Файл не должен быть пустым");
        }
        String filename = file.getOriginalFilename();
        if(filename == null || !filename.toLowerCase().endsWith(".xlsx")){
            return ResponseEntity
                    .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Только .xlsx файлы поддерживаются");
        }

        try{
            excelImportService.updateMissingFromExcel(file);
            return ResponseEntity.ok("Импорт успешно выполнен для файла: " + filename);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка данных: " + iae.getMessage());
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при импорте: " + e.getMessage());
        }
    }


    @PostMapping(value = "/csv", consumes = "multipart/form-data")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Файл не должен быть пустым");
        }
        String filename = file.getOriginalFilename();
        if(filename == null || (!filename.toLowerCase().endsWith(".csv") && !filename.toLowerCase().endsWith(".md"))){
            return ResponseEntity
                    .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Только .csv или .md файлы поддерживаются");
        }

        try{
            java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("import-", ".csv");
            java.nio.file.Files.write(tempFile, file.getBytes());
            csvImportService.importFromCsvFile(tempFile);
            java.nio.file.Files.deleteIfExists(tempFile);
            return ResponseEntity.ok("CSV импорт успешно выполнен для файла: " + filename);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка данных: " + iae.getMessage());
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при импорте: " + e.getMessage());
        }
    }

}
