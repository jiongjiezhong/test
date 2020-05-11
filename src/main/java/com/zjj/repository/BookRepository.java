package com.zjj.repository;

import com.zjj.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {

    @Query(value = "select book_name from book", nativeQuery = true)
    List<String> findAllBookName();
}
