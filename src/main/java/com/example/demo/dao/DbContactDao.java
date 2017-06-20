package com.example.demo.dao;

import com.example.demo.entity.DbContact;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DbContactDao extends CrudRepository<DbContact, Integer> {

    List<DbContact> findByName(String name);

    List<DbContact> findByPhone(String name);

}
