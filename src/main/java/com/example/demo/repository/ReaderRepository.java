package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Reader;

@Repository
public interface ReaderRepository extends CrudRepository<Reader, Long> {
}
