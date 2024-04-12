package com.healthcare.system.repositories;

import com.healthcare.system.entities.Complaint;
import com.healthcare.system.exceptions.WrongCredentials;

import java.rmi.ServerException;
import java.util.List;

public interface ComplaintRepository {
    Complaint findById(int id) throws WrongCredentials, ServerException;

    void deleteById(int id) throws WrongCredentials, ServerException;

    void update(Complaint complaint) throws WrongCredentials, ServerException;

    List<Complaint> findAll() throws ServerException;

    void save(Complaint complaint) throws WrongCredentials, ServerException;
}
