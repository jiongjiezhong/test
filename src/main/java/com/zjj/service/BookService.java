package com.zjj.service;

import com.zjj.dto.BookDto;

import java.util.List;

public interface BookService {
    List<String> getName();

    Boolean addBook(BookDto dto);

    void testAbstractHandler(String type);
}
