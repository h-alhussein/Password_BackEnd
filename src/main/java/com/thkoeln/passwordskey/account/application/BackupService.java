package com.thkoeln.passwordskey.account.application;

import com.thkoeln.passwordskey.account.domain.Account;
import com.thkoeln.passwordskey.collection.application.CollectionService;
import com.thkoeln.passwordskey.collection.domain.CollectionRepository;
import com.thkoeln.passwordskey.group.domain.GroupRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BackupService {
    private final AccountService accountService;
    private final CollectionRepository collectionRepository;
    private final CollectionService collectionService;
    private final GroupRepository groupRepository;

    public ResponseEntity<?> importCSV(MultipartFile file, String groupID) {

        if (groupID == null || groupRepository.findById(groupID).isEmpty()) {
            return new ResponseEntity<>("Invalid Group, Please select a Group", HttpStatus.CONFLICT);
        }

        if (file == null) {
            return new ResponseEntity<>("Please choose a CSV-File", HttpStatus.CONFLICT);

        }
        BufferedReader reader = null;
        String line = "";
        try {
            XSSFWorkbook workbook= new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet= workbook.getSheetAt(0);
            ;

            int rowCount= sheet.getPhysicalNumberOfRows();

            for (int i = 1 ; i<rowCount;i++) {
                AccountDto accountDto = new AccountDto();
                accountDto.setGroupId(groupID);
                accountDto.setName(sheet.getRow(i).getCell(0).getStringCellValue());
                accountDto.setUsername(sheet.getRow(i).getCell(1).getStringCellValue());
                accountDto.setPassword(sheet.getRow(i).getCell(2).getStringCellValue());
                accountDto.setWebsite(sheet.getRow(i).getCell(3).getStringCellValue());
                accountDto.setNotes(sheet.getRow(i).getCell(4).getStringCellValue());
                accountService.addAccount(accountDto);

            }
            workbook.close();
            return new ResponseEntity<>("Successfully", HttpStatus.OK);

            } catch (IOException ex) {
            return new ResponseEntity<>("There is an Error , Please try again later", HttpStatus.CONFLICT);
        }

    }







    public ResponseEntity exportCSV(String id, HttpServletResponse response) throws FileNotFoundException {
        if (id == null || collectionRepository.findById(id).isEmpty()) {
            return new ResponseEntity("Invalid Collection, Please select a valid Collection", HttpStatus.CONFLICT);
        }
        String name = "import " + collectionRepository.findById(id).get().getName().toString() + new Date().getTime();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(name);
        int rowCount = 1;
         Row head =sheet.createRow(0);
         head.createCell(0).setCellValue("Account");
        head.createCell(1).setCellValue("Login Name");
        head.createCell(2).setCellValue("Password");
        head.createCell(3).setCellValue("Website");
        head.createCell(4).setCellValue("Notes");

        for (Account i : accountService.decryptList(collectionService.findallAccounts(id))) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(i.getName());
           row.createCell(1).setCellValue(i.getUsername());
           row.createCell(2).setCellValue(i.getPassword());
            row.createCell(3).setCellValue(i.getWebsite());
            row.createCell(4).setCellValue(i.getNotes());


        }


        try  {
            response.setContentType("application/octet-stream");
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            return new ResponseEntity<>(response, HttpStatus.OK);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
