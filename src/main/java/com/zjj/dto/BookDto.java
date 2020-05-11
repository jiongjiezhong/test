package com.zjj.dto;

import java.math.BigDecimal;

public class BookDto {

    private String  bookName;
    private BigDecimal bookPrize;

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
