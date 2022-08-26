package Model;

import javax.persistence.*;

@Entity
@Table(name="test_db.books")
public class Book {
    @Id
    @Column(name="bookID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookID;
    @Column(name="book_name")
    private String book_name;
    @Column(name="book_authorID")
    private int book_authorID;
    @Column(name="book_publish_year")
    private String publish_year;
    @Column(name="book_in_stock")
    private boolean book_in_stock = true;
    @Column(name="book_count")
    private int book_count;
    @Column(name="book_ISBN")
    private String book_ISBN;

    public void setBookISBN(String ISBN) {
        this.book_ISBN = ISBN;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public void setBook_authorID(int book_authorID) {
        this.book_authorID = book_authorID;
    }

    public void setBook_count(int book_count) {
        this.book_count = book_count;
    }

    public void setPublish_year(String publish_year) {
        this.publish_year = publish_year;
    }

    public void setInStock(boolean stock) {this.book_in_stock = stock;}

    public int getBookID() {
        return bookID;
    }

    public int getBook_authorID() {
        return book_authorID;
    }

    public String getPublish_year() {
        return publish_year;
    }

    public String getBook_name() {
        return book_name;
    }

    public int getBook_count() {
        return book_count;
    }

    public boolean getInStock() {return book_in_stock; }

    public String getBookISBN() {
        return book_ISBN;
    }
}
