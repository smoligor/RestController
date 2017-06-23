package com.example.demo.service;

import com.example.demo.entity.Contact;
import com.example.demo.entity.RawContact;

public interface ContactService {

    Contact createContact(RawContact rawContact);

    RawContact getContact(String name);

    Contact updateContact(RawContact rawContact);

    Contact deleteContact(String name);

}
