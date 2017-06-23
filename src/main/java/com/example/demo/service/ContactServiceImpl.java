package com.example.demo.service;

import com.example.demo.dao.DbContactDao;
import com.example.demo.entity.Contact;
import com.example.demo.entity.DbContact;
import com.example.demo.entity.RawContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    DbContactDao dbContactDao;

    @Transactional
    @Override
    public Contact createContact(RawContact rawContact) {
        List<DbContact> dbContacts = new ArrayList<>();
        Contact contact = transformRawContactToContact(rawContact);
        List<String> phones = new ArrayList<>(Arrays.asList(contact.getPhones().split("\\s*,\\s*")));
        phones.forEach(phone -> {
            DbContact dbContact = DbContact.builder().name(rawContact.getName()).phone(phone).build();
            dbContactDao.save(dbContact);
            dbContacts.add(dbContact);
        });
        return transformDbContactsToContact(dbContacts);
    }

    @Transactional
    @Override
    public RawContact getContact(String name) {
        List<DbContact> dbContacts = dbContactDao.findByName(name);
        if (dbContacts.isEmpty()) {
            return null;
        }
        return RawContact.builder().name(name).phones(dbContacts
                .stream().map(DbContact::getPhone).collect(Collectors.toList())).build();
    }

    @Transactional
    @Override
    public Contact updateContact(RawContact rawContact) {
        if (!dbContactDao.findByName(rawContact.getName()).isEmpty()) {
            List<DbContact> dbContacts = dbContactDao.findByName(rawContact.getName());
            if (dbContacts.isEmpty()) {
                return transformDbContactsToContact(dbContacts);
            }
            dbContactDao.delete(dbContacts);
            createContact(rawContact);
            return transformDbContactsToContact(dbContacts);
        } else {
            List<DbContact> dbContacts = new ArrayList<>();
            rawContact.getPhones().forEach(phone ->
                    dbContactDao.findByPhone(phone).forEach(dbContact -> {
                        dbContact = DbContact.builder().name(rawContact.getName()).phone(dbContact.getPhone()).build();
                        dbContacts.add(dbContact);
                        dbContactDao.delete(dbContactDao.findByPhone(phone));
                        dbContactDao.save(dbContact);
                    }));
            return transformDbContactsToContact(dbContacts);
        }
    }

    @Transactional
    @Override
    public Contact deleteContact(String name) {
        List<DbContact> dbContacts = dbContactDao.findByName(name);
        Contact contact = transformDbContactsToContact(dbContacts);
        dbContactDao.delete(dbContacts);
        return contact;
    }

    private Contact transformRawContactToContact(RawContact rawContact) {
        String contactName = rawContact.getName();
        String phoneString = "";
        for (String phone : rawContact.getPhones()) {
            phoneString = phoneString.concat(phone) + ", ";
        }
        phoneString = deleteExtraComma(phoneString);
        return Contact.builder().name(contactName).phones(phoneString).build();
    }

    private Contact transformDbContactsToContact(List<DbContact> dbContacts) {
        if (dbContacts.isEmpty()) {
            return null;
        }
        String contactName = dbContacts.get(0).getName();
        String phoneString = dbContacts.stream().map(dbContact ->
                dbContact.getPhone() + ", ").collect(Collectors.joining());
        phoneString = deleteExtraComma(phoneString);
        return Contact.builder().name(contactName).phones(phoneString).build();
    }

    private String deleteExtraComma(String phoneString) {
        phoneString = phoneString.trim();
        if (phoneString.length() > 0 && phoneString.charAt(phoneString.length() - 1) == ',') {
            phoneString = phoneString.substring(0, phoneString.length() - 1);
        }
        return phoneString;
    }
}

