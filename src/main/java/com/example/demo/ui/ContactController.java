package com.example.demo.ui;

import com.example.demo.entity.DbContact;
import com.example.demo.entity.RawContact;
import com.example.demo.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class ContactController {

    @Autowired
    ContactService contactService;

    @PostMapping("/addContact")
    public ResponseEntity<?> addContact(@RequestBody RawContact rawContact) {
        List<DbContact> dbContacts = contactService.createContact(rawContact);
        if (dbContacts.isEmpty()) {
            return new ResponseEntity<>("Can't create contact", BAD_REQUEST);
        }
        return new ResponseEntity<>("Contact created", OK);
    }

    @GetMapping("/getContact/{name}")
    public ResponseEntity<?> getContact(@PathVariable String name) {
        RawContact rawContact = contactService.getContact(name);
        if (rawContact == null) {
            return new ResponseEntity<>("Can't find contact", BAD_REQUEST);
        }
        return new ResponseEntity<>(rawContact, OK);
    }

    @PostMapping("/updateContact")
    public ResponseEntity<?> updateContact(@RequestBody RawContact rawContact) {
        List<DbContact> dbContacts = contactService.updateContact(rawContact);
        if (dbContacts.isEmpty()) {
            return new ResponseEntity<>("Can't update contact", BAD_REQUEST);
        }
        return new ResponseEntity<>("Contact updated", OK);
    }

    @DeleteMapping("/deleteContact/{name}")
    public ResponseEntity<?> deleteContact(@PathVariable String name) {
        List<DbContact> dbContacts = contactService.deleteContact(name);
        if (dbContacts.isEmpty()) {
            return new ResponseEntity<>("Can't delete contact", BAD_REQUEST);
        }
        return new ResponseEntity<>("Contact deleted", OK);
    }
}
