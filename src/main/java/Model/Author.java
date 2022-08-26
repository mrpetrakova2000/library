package Model;

import javax.persistence.*;

@Entity
@Table(name="test_db.authors")
public class Author extends Person{
    @Id
    @Column(name="authorID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authorID;

    public int getAuthorID() {
        return authorID;
    }
}
