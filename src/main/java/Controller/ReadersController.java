package Controller;
import Model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReadersController {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
    EntityManager em = emf.createEntityManager();
    public class IsTakenException extends Exception {
        public IsTakenException() {super("This reader has already taken this book!");}
    }
    public void checkBookIsTaken(List<Leasing> l) throws IsTakenException {
        if (l.size() != 0) throw new IsTakenException();
    }
    public boolean addToDB(Reader r){
        try {
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
            return true;
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Not unique passport number!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public Reader getById(int id){
        em.getTransaction().begin();
        Reader r = em.find(Reader.class, id);
        em.getTransaction().commit();
        return r;
    }
    public boolean delete(int id){
        Reader r = getById(id);
        em.getTransaction().begin();
        em.remove(r);
        em.getTransaction().commit();
        Reader r1 = getById(id);
        return r1 == null;
    }
    public String[][] show(){
        em.getTransaction().begin();
        List<Reader> re = em.createQuery("SELECT r from Reader r").getResultList();
        String[][] readers_data = new String[re.size()][4];
        for (int i = 0; i < re.size(); i++){
            readers_data[i][0] = String.valueOf(re.get(i).getReaderID());
            readers_data[i][1] = re.get(i).getName();
            readers_data[i][2] = re.get(i).getLastName();
            readers_data[i][3] = String.valueOf(re.get(i).getReader_doc());
        }
        em.getTransaction().commit();
        return readers_data;
    }
    public boolean edit(int id, String[] fields){
        Reader r = getById(id);
        try{
            em.getTransaction().begin();
            r.setName(fields[0]);
            r.setLastName(fields[1]);
            r.setReader_doc(Integer.parseInt(fields[2]));
            em.getTransaction().commit();
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Not unique passport number!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    public int count(){
        em.getTransaction().begin();
        List<Reader> list = em.createQuery("SELECT r from Reader r").getResultList();
        em.getTransaction().commit();
        return list.size();
    }
    public void GetBook(int readerID, int bookID) throws IsTakenException {
        em.getTransaction().begin();
        List<Leasing> lTest = em.createQuery("SELECT l from Leasing l WHERE l.lease_readerID="+readerID+" AND " +
                "l.lease_finish_date="+null+" AND l.lease_bookID="+bookID).getResultList();
        checkBookIsTaken(lTest);
        Leasing le = new Leasing();
        le.setLease_readerID(readerID);
        le.setLease_bookID(bookID);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String start_date = dateFormat.format(date);
        le.setLease_start_date(start_date);
        em.persist(le);
        em.getTransaction().commit();
    }

    public void ReturnBook(int readerID, int bookID){
        em.getTransaction().begin();
        Leasing l = (Leasing) em.createQuery("SELECT l from Leasing l WHERE l.lease_bookID="+bookID+
                " AND l.lease_readerID="+readerID+" AND l.lease_finish_date="+null).getResultList().get(0);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String finish_date = dateFormat.format(date);
        l.setLease_finish_date(finish_date);
        em.getTransaction().commit();
    }

    public String[][] PrintMyBooks(int readerID){
        em.getTransaction().begin();
        List<Leasing> leasings = em.createQuery("SELECT l from Leasing l WHERE l.lease_readerID="+readerID+" AND " +
                "l.lease_finish_date="+null).getResultList();
        String[][] MyBooks = new String[leasings.size()][4];
        for (int i = 0; i < leasings.size(); i++){
            Book b = em.find(Book.class, leasings.get(i).getLease_bookID());
            MyBooks[i][0] = b.getBookISBN();
            MyBooks[i][1] = b.getBook_name();
            Author a = em.find(Author.class, b.getBook_authorID());
            MyBooks[i][2] = a.getName() + " " + a.getLastName();
            MyBooks[i][3] = leasings.get(i).getLease_start_date();
        }
        em.getTransaction().commit();
        return MyBooks;
    }
}
