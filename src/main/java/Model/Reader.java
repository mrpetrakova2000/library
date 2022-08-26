package Model;

import Controller.ReadersController;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="test_db.readers")
public class Reader extends Person{

    @Id
    @Column(name="readerID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int readerID;

    @Column(name="reader_doc")
    private int reader_doc;

    public int getReaderID() {
        return readerID;
    }
    public int getReader_doc() { return reader_doc; }
    public void setReader_doc(int doc) { this.reader_doc = doc; }

    public void GetBook(int bookID) throws ReadersController.IsTakenException {
       ReadersController rc = new ReadersController();
       rc.GetBook(readerID, bookID);
    }
    public void ReturnBook(int bookID){
        ReadersController rc = new ReadersController();
        rc.ReturnBook(readerID, bookID);
    }

    public String[][] PrintMyBooks(){
        ReadersController rc = new ReadersController();
        return rc.PrintMyBooks(readerID);
    }
}
