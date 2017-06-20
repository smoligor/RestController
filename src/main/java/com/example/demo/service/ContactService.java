package com.example.demo.service;

import com.example.demo.entity.DbContact;
import com.example.demo.entity.RawContact;

import java.util.List;

public interface ContactService {

    List<DbContact> createContact(RawContact rawContact);

    RawContact getContact(String name);

    List<DbContact> updateContact(RawContact rawContact);

    List<DbContact> deleteContact(String name);

}
