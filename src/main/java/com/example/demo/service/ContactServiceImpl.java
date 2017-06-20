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
    public List<DbContact> createContact(RawContact rawContact) {
        List<DbContact> dbContacts = new ArrayList<>();
        Contact contact = transformRawContactToContact(rawContact);
        List<String> phones = new ArrayList<>(Arrays.asList(contact.getPhones().split("\\s*,\\s*")));
        phones.forEach(phone -> {
            DbContact dbContact = new DbContact();
            dbContact.setName(contact.getName());
            dbContact.setPhone(phone);
            dbContactDao.save(dbContact);
            dbContacts.add(dbContact);
        });
        return dbContacts;
    }

    @Transactional
    @Override
    public RawContact getContact(String name) {
        RawContact rawContact = new RawContact();
        List<DbContact> dbContacts = dbContactDao.findByName(name);
        if (dbContacts.isEmpty()) {
            return null;
        }
        rawContact.setName(name);
        rawContact.setPhones(dbContacts
                .stream().map(DbContact::getPhone).collect(Collectors.toList()));
        return rawContact;
    }

    @Transactional
    @Override
    public List<DbContact> updateContact(RawContact rawContact) {
        if (!dbContactDao.findByName(rawContact.getName()).isEmpty()) {
            List<DbContact> dbContacts = dbContactDao.findByName(rawContact.getName());
            if (dbContacts.isEmpty()) {
                return dbContacts;
            }
            dbContactDao.delete(dbContacts);
            createContact(rawContact);
            return dbContacts;
        } else {
            List<DbContact> dbContacts = new ArrayList<>();
            rawContact.getPhones().forEach(phone ->
                    dbContactDao.findByPhone(phone).forEach(dbContact -> {
                        dbContact.setName(rawContact.getName());
                        dbContacts.add(dbContact);
                        dbContactDao.save(dbContact);
                    }));
            return dbContacts;
        }
    }

    @Transactional
    @Override
    public List<DbContact> deleteContact(String name) {
        List<DbContact> dbContacts = dbContactDao.findByName(name);
        dbContactDao.delete(dbContacts);
        return dbContacts;
    }

    private Contact transformRawContactToContact(RawContact rawContact) {
        Contact contact = new Contact();
        contact.setName(rawContact.getName());
        String phoneString = "";
        for (String phone : rawContact.getPhones()) {
            phoneString = phoneString.concat(phone) + ", ";
        }
        phoneString = phoneString.trim();
        if (phoneString.length() > 0 && phoneString.charAt(phoneString.length() - 1) == ',') {
            phoneString = phoneString.substring(0, phoneString.length() - 1);
        }
        contact.setPhones(phoneString);
        return contact;
    }
}
