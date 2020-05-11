package com.zjj.service.impl;

import com.zjj.dto.BookDto;
import com.zjj.entity.Book;
import com.zjj.repository.BookRepository;
import com.zjj.service.BookService;
import com.zjj.service.header.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<String> getName() {
        List<String> allBookName = bookRepository.findAllBookName();
        return allBookName;
    }

    @Override
    public Boolean addBook(BookDto dto) {
        try {
            Book book = new Book();
            book.setBookName(dto.getBookName());
            book.setBookPrize(dto.getBookPrize());
            bookRepository.save(book);
            return true;
        } catch (Exception e) {
            logger.error("exception", e);
            return false;
        }
    }

    @Override
    public void testAbstractHandler(String type) {
//        AbstractHandler handler = new AbstractHandler() {
//            @Override
//            public void saveEntity(String type) {
//
//            }
//        };
//        handler.saveEntity(type);
    }
}
