import org.junit.*;
import Model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestFrame {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
    EntityManager em = emf.createEntityManager();

    @Test
    public void TestAddReader(){
        Reader r = new Reader();
        r.setName("Michail");
        r.setLastName("Grechuhin");
        r.setReader_doc(4661);
        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();
        Assert.assertNotNull(em.find(Reader.class, r.getReaderID()));
    }
}
