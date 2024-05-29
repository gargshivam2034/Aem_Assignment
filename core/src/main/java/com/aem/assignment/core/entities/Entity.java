package com.aem.assignment.core.entities;

public class Entity {

   private String bookAuthor;
   private String bookName;
   private  String bookPrice;
    private String bookPath;

    public Entity(String bookAuthor,String bookName,String bookPrice)
    {
        this.bookAuthor=bookAuthor;
        this.bookName=bookName;
        this.bookPrice=bookPrice;

    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public Entity(String bookAuthor, String bookName)
    {
        this.bookAuthor=bookAuthor;
        this.bookName=bookName;

    }
    public Entity(String bookAuthor,String bookName,String bookPrice,String bookPath)
    {
        this.bookAuthor=bookAuthor;
        this.bookName=bookName;
        this.bookPath=bookPath;
        this.bookPrice=bookPrice;

    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public String getBookPath() {
        return bookPath;
    }
}
