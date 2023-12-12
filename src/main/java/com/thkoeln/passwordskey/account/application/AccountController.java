package com.thkoeln.passwordskey.account.application;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final BackupService backupService;

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getAllByGroupId(@PathVariable String groupId) {
        return new ResponseEntity<>(accountService.getAllByGroupId(groupId), HttpStatus.OK) ;
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @ModelAttribute AccountDto accountDto) {
        accountService.addAccount(accountDto);
        return new ResponseEntity<>("Your add has been reset successfully .", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @ModelAttribute AccountDto accountDto) {
        return accountService.updateAccount(id, accountDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        return accountService.deleteAccount(id);
    }
    @PostMapping("/fav")
    public ResponseEntity<?> changeFav(@RequestParam("id") String id) {
        return accountService.changeFav(id);
    }

    @GetMapping("/fav")
    public ResponseEntity<?> getFav() {
        return accountService.getFav();
    }

    @GetMapping("/csvexport/{id}")
    public ResponseEntity<?> exportCSV(@PathVariable String id, HttpServletResponse response)
            throws Exception {

        return backupService.exportCSV(id,response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> importing(@ModelAttribute("file") MultipartFile file,@PathVariable String id){
        return backupService.importCSV(file,id);
    }


}
