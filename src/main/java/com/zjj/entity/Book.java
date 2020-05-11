package com.zjj.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "book")
public class Book {

    private Integer id;

    private String  bookName;

    private BigDecimal bookPrize;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public BigDecimal getBookPrize() {
        return bookPrize;
    }

    public void setBookPrize(BigDecimal bookPrize) {
        this.bookPrize = bookPrize;
    }
}
