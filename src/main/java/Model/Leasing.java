package Model;

import javax.persistence.*;

@Entity
@Table(name="test_db.leasing")
public class Leasing {
    @Id
    @Column(name="leaseID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int leaseID;
    @Column(name="lease_readerID")
    private int lease_readerID;
    @Column(name="lease_bookID")
    private int lease_bookID;
    @Column(name="lease_start_date")
    private String lease_start_date;
    @Column(name="lease_finish_date")
    private String lease_finish_date;


    public void setLease_readerID(int lease_readerID) {
        this.lease_readerID = lease_readerID;
    }

    public void setLease_bookID(int lease_bookID) {
        this.lease_bookID = lease_bookID;
    }

    public void setLease_start_date(String lease_start_date) {
        this.lease_start_date = lease_start_date;
    }

    public void setLease_finish_date(String lease_finish_date) {
        this.lease_finish_date = lease_finish_date;
    }

    public int getLeaseID() {
        return leaseID;
    }

    public int getLease_readerID() {
        return lease_readerID;
    }

    public int getLease_bookID() {
        return lease_bookID;
    }

    public String getLease_start_date() {
        return lease_start_date;
    }

    public String getLease_finish_date() {
        return lease_finish_date;
    }
}
