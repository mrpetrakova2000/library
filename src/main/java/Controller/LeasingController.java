package Controller;
import Model.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LeasingController {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
    EntityManager em = emf.createEntityManager();

    public Leasing findByBook(int id){
        em.getTransaction().begin();
        List <Leasing> le = em.createQuery("SELECT l FROM Leasing l WHERE l.lease_bookID="+id+
                " AND l.lease_finish_date="+null).getResultList();
        em.getTransaction().commit();
        if (le.size() > 0) return le.get(0);
        else return null;
    }
    public int readersForMonth() throws ParseException {
        LocalDate date = LocalDate.now();
        LocalDate date1 = LocalDate.now().minusMonths(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        em.getTransaction().begin();
        List<Leasing> le = em.createQuery("SELECT l FROM Leasing l").getResultList();
        em.getTransaction().commit();
        int[] readers = new int[le.size()];
        for (int i = 0, k = 1; i < le.size(); i++){
            String dateStart = le.get(i).getLease_start_date();
            if (LocalDate.parse(dateStart,format).isBefore(date.plusDays(1)) &&
                    date1.isBefore(LocalDate.parse(dateStart,format).plusDays(1))){
                int reID = le.get(i).getLease_readerID();
                boolean consist = false;
                if (i == 0) readers[i] = reID;
                for (int reader : readers) {
                    if (reader == reID) {
                        consist = true;
                        break;
                    }

                }
                if (!consist) readers[k++] = reID;
            }
        }
        int len;
        for (len = 0; len < readers.length && readers[len] > 0; len++);

        return len;
    }
    public int booksForMonth(){
        LocalDate date = LocalDate.now();
        LocalDate date1 = LocalDate.now().minusMonths(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        em.getTransaction().begin();
        List<Leasing> le = em.createQuery("SELECT l FROM Leasing l").getResultList();
        int count = 0;
        for (int i = 0; i < le.size(); i++){
            String dateStart = le.get(i).getLease_start_date();
            if (LocalDate.parse(dateStart,format).isBefore(date.plusDays(1)) &&
                    date1.isBefore(LocalDate.parse(dateStart,format).plusDays(1))){
                    count++;
            }
        }
        em.getTransaction().commit();
        return count;
    }

    public String[][] Info(){
        em.getTransaction().begin();
        List <Leasing> le = em.createQuery("SELECT l FROM Leasing l WHERE l.lease_finish_date="+null).getResultList();
        String[][] info = new String[le.size()][5];
        for (int i = 0; i < info.length; i++){
            Reader r = em.find(Reader.class, le.get(i).getLease_readerID());
            info[i][0] = String.valueOf(r.getReader_doc());
            info[i][1] = r.getName() + " " + r.getLastName();
            Book b = em.find(Book.class, le.get(i).getLease_bookID());
            info[i][2] = b.getBook_name();
            Author a = em.find(Author.class, b.getBook_authorID());
            info[i][3] = a.getName() + " " + a.getLastName();
            info[i][4] = le.get(i).getLease_start_date();
        }
        em.getTransaction().commit();
        return info;
    }

    public Leasing findByReader(int id) {
        em.getTransaction().begin();
        List <Leasing> le = em.createQuery("SELECT l FROM Leasing l WHERE l.lease_readerID="+id+
                " AND l.lease_finish_date="+null).getResultList();
        em.getTransaction().commit();
        if (le.size() > 0) return le.get(0);
        else return null;
    }

    public void deleteB(int id) {
        em.getTransaction().begin();
        List <Leasing> le = em.createQuery("SELECT l FROM Leasing l WHERE l.lease_bookID="+id).getResultList();
        for (Leasing l : le){
            em.remove(l);
        }
        em.getTransaction().commit();
    }

    public void deleteR(int id) {
        em.getTransaction().begin();
        List <Leasing> le = em.createQuery("SELECT l FROM Leasing l WHERE l.lease_readerID="+id).getResultList();
        for (Leasing l : le){
            em.remove(l);
        }
        em.getTransaction().commit();
    }
}
