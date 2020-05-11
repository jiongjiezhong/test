package com.zjj.controller;

import com.zjj.dto.BookDto;
import com.zjj.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/book")
@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/testName")
    public String refreshGoodsNumCache() {
        return "hello";
    }

    @GetMapping("/getName")
    public List<String> getName() {
        return bookService.getName();
    }

    @PostMapping("/addBook")
    public Boolean addBook(@RequestBody BookDto dto) {
        return bookService.addBook(dto);
    }

    @GetMapping("/testAbstractHandler")
    public void testAbstractHandler(String type) {
        bookService.testAbstractHandler(type);
    }

}
