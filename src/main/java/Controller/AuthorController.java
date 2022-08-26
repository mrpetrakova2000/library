package Controller;
import Model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.*;
import java.util.List;

public class AuthorController {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
    EntityManager em = emf.createEntityManager();
    public void addToDB(Author a){
        em.getTransaction().begin();
        em.persist(a);
        em.getTransaction().commit();
    }
    public Author getAuthor(String[] author_name){
        em.getTransaction().begin();
        Author select = (Author) em.createQuery("SELECT a from Author a where a.name='" +author_name[0] +
                "' and a.lastName='"+author_name[1]+"'").getResultList().get(0);
        em.getTransaction().commit();
        return select;
    }
    public Author getById(int id){
        em.getTransaction().begin();
        Author a = em.find(Author.class, id);
        em.getTransaction().commit();
        return a;
    }
    public String[][] show() {
        em.getTransaction().begin();
        List<Author> au = em.createQuery("SELECT a from Author a").getResultList();
        String[][] author_data = new String[au.size()][3];
        for (int i = 0; i < au.size(); i++){
            author_data[i][0] = String.valueOf(au.get(i).getAuthorID());
            author_data[i][1] = au.get(i).getName();
            author_data[i][2] = au.get(i).getLastName();
        }
        em.getTransaction().commit();
        return author_data;
    }
    public boolean edit(int id, String[] fields){
        Author a = getById(id);
        em.getTransaction().begin();
        a.setName(fields[0]);
        a.setLastName(fields[1]);
        em.getTransaction().commit();
        return true;
    }
}
