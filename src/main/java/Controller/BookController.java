package Controller;
import Model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BookController {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
    EntityManager em = emf.createEntityManager();
    public void addToDB(Book b){
        em.getTransaction().begin();
        String ISBN = "";
        if (b.getBook_name().contains(" ")) {
            String[] firstLetter = b.getBook_name().split(" ");
            ISBN = ISBN + firstLetter[0].charAt(0) + firstLetter[1].charAt(0);
        }
        else {
            ISBN = b.getBook_name().substring(0,2);
        }
        b.setBookISBN(ISBN);
        em.persist(b);
        em.getTransaction().commit();

        em.getTransaction().begin();
        ISBN = ISBN + b.getBookID();
        b.setBookISBN(ISBN);
        em.getTransaction().commit();
    }
    public Book getById(int id){
        em.getTransaction().begin();
        Book b = em.find(Book.class, id);
        em.getTransaction().commit();
        return b;
    }
    public boolean delete(int id){
        Book b = getById(id);
        em.getTransaction().begin();
        em.remove(b);
        em.getTransaction().commit();
        Book b1 = getById(id);
        if (b1 == null) return true;
        else return false;
    }
    public String[][] show(){
        em.getTransaction().begin();
        List<Book> bo = em.createQuery("SELECT b from Book b").getResultList();
        String[][] books_data = new String[bo.size()][7];
        for (int i = 0; i < bo.size(); i++){
            Author a = em.find(Author.class, bo.get(i).getBook_authorID());
            books_data[i][0] = String.valueOf(bo.get(i).getBookID());
            books_data[i][1] = String.valueOf(bo.get(i).getBookISBN());
            books_data[i][2] = a.getName() + " " + a.getLastName();
            books_data[i][3] = bo.get(i).getBook_name();
            books_data[i][4] = String.valueOf(bo.get(i).getPublish_year());
            books_data[i][5] = String.valueOf(bo.get(i).getBook_count());
            if (bo.get(i).getInStock()) books_data[i][6] = "+";
            else books_data[i][6] = "-";
        }
        em.getTransaction().commit();
        return books_data;
    }
    public Book findByName(String name){
        em.getTransaction().begin();
        Book b = (Book) em.createQuery("SELECT b from Book b WHERE b.book_name='"+name+"'").getResultList().get(0);
        em.getTransaction().commit();
        return b;
    }

    public Book findByISBN(String ISBN){
        em.getTransaction().begin();
        List<Book> lb = em.createQuery("SELECT b from Book b WHERE b.book_ISBN='"+ISBN+"'").getResultList();
        Book b;
        if (lb.size() > 0) {b = lb.get(0);}
        else b = null;
        em.getTransaction().commit();
        return b;
    }

    public boolean edit(int id, String[] fields){
        Book b = getById(id);
        try {
            em.getTransaction().begin();
            b.setBookISBN(fields[0]);
            b.setBook_name(fields[1]);
            b.setPublish_year(fields[2]);
            b.setBook_count(Integer.parseInt(fields[3]));
            if (fields[3].equals("0")) b.setInStock(false);
            else b.setInStock(true);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Not unique ISBN!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void takeBook(int bookID){
        Book b = getById(bookID);
        int prevCount = b.getBook_count();
        em.getTransaction().begin();
        b.setBook_count(prevCount-1);
        if (b.getBook_count() == 0) b.setInStock(false);
        em.getTransaction().commit();
    }

    public void returnBook(int bookID){
        Book b = getById(bookID);
        int prevCount = b.getBook_count();
        em.getTransaction().begin();
        b.setBook_count(prevCount+1);
        b.setInStock(true);
        em.getTransaction().commit();
    }
    public String[][] findByAuthor(int id){
        em.getTransaction().begin();
        List<Book> bo = (List<Book>) em.createQuery("SELECT b from Book b WHERE b.book_authorID="+id).getResultList();
        String[][] books_data = new String[bo.size()][7];
        for (int i = 0; i < bo.size(); i++){
            Author a = em.find(Author.class, bo.get(i).getBook_authorID());
            books_data[i][0] = String.valueOf(bo.get(i).getBookID());
            books_data[i][1] = String.valueOf(bo.get(i).getBookISBN());
            books_data[i][2] = a.getName() + " " + a.getLastName();
            books_data[i][3] = bo.get(i).getBook_name();
            books_data[i][4] = String.valueOf(bo.get(i).getPublish_year());
            books_data[i][5] = String.valueOf(bo.get(i).getBook_count());
            if (bo.get(i).getInStock()) books_data[0][6] = "+";
            else books_data[i][6] = "-";
        }
        em.getTransaction().commit();
        return books_data;
    }

    public String[][] bookWithID(int id){
        Book bo = getById(id);
        em.getTransaction().begin();
        String[][] books_data = new String[1][7];
        Author a = em.find(Author.class, bo.getBook_authorID());
        books_data[0][0] = String.valueOf(bo.getBookID());
        books_data[0][1] = String.valueOf(bo.getBookISBN());
        books_data[0][2] = a.getName() + " " + a.getLastName();
        books_data[0][3] = bo.getBook_name();
        books_data[0][4] = String.valueOf(bo.getPublish_year());
        books_data[0][5] = String.valueOf(bo.getBook_count());
        if (bo.getInStock()) books_data[0][6] = "+";
        else books_data[0][6] = "-";
        em.getTransaction().commit();
        return books_data;
    }
}
