package entity;

public class Book {
    private String bookId;
    private String bookName;
    private String bookType; // 图书类型：哲学、宗教等
    private boolean canBorrow; // 是否可借出

    public Book(String bookId, String bookName, String bookType) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookType = bookType;
        this.canBorrow = true;
    }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
    public String getBookType() { return bookType; }
    public void setBookType(String bookType) { this.bookType = bookType; }
    public boolean isCanBorrow() { return canBorrow; }
    public void setCanBorrow(boolean canBorrow) { this.canBorrow = canBorrow; }
}
