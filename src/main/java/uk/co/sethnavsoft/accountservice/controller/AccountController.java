package uk.co.sethnavsoft.accountservice.controller;

import io.swagger.annotations.SwaggerDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import uk.co.sethnavsoft.accountservice.dao.AccountRepository;
import uk.co.sethnavsoft.accountservice.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Account service endpoint for accessing Account information through RESTful interface.
 */
@RestController()
@Slf4j
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;


    @GetMapping(value = "/rest/account/json", produces = "application/json")
    public ResponseEntity<List<Account>> getAllAccounts() {
        final List<Account> accounts = new ArrayList<>();
        log.info("Getting all accounts");
        try {
            accountRepository.findAll().forEach(accounts::add);
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed get all accounts", e);
            return new ResponseEntity<>(accounts, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/rest/account/json", consumes = "application/json", produces = "text/html")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        try {
            log.info("Creating account");
            Assert.isNull(account.getId(), "Account Id should not be set.");
            accountRepository.save(account);
            return new ResponseEntity<>("Account has been successfully added", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            log.error("Failed to create account [invalid data]", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Failed to create account", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(value = "/rest/account/json/{id}", produces = "text/html")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Long id) {
        try {
            log.info("deleting account");
            accountRepository.deleteById(id);
            return new ResponseEntity<>("Account successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to delete account", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
