package entity;

import java.util.Date;

public class BorrowRecord {
    private String recordId;
    private String username;
    private Book book;
    private Date borrowDate;
    private Date returnDate; // null代表未归还
    private int overdueDays; // 超期天数

    public BorrowRecord(String recordId, String username, Book book, Date borrowDate) {
        this.recordId = recordId;
        this.username = username;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = null;
        this.overdueDays = 0;
    }

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public Date getBorrowDate() { return borrowDate; }
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    public int getOverdueDays() { return overdueDays; }
    public void setOverdueDays(int overdueDays) { this.overdueDays = overdueDays; }
}
