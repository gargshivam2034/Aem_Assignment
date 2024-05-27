package com.aem.assignment.core.models.impl;

public class Entity {

    String BookAuthor;
    String BookName;
    String BookPrice;
    String BookPath;

    public void setBookAuthor(String bookAuthor) {
        BookAuthor = bookAuthor;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public void setBookPrice(String bookPrice) {
        BookPrice = bookPrice;
    }

    public String getBookAuthor() {
        return BookAuthor;
    }

    public String getBookName() {
        return BookName;
    }

    public String getBookPrice() {
        return BookPrice;
    }

    public String getBookPath() {
        return BookPath;
    }

    public void setBookPath(String bookPath) {
        BookPath = bookPath;
    }
}
