package Model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Person {
    @Column(name="name")
    private String name;
    @Column(name="lastName")
    private String lastName;
    public boolean setName(String name) {
        this.name = name;
        return true;
    }
    public String getName() {
        return name;
    }
    public boolean setLastName(String lastName) {
        this.lastName = lastName;
        return true;
    }
    public String getLastName() {
        return lastName;
    }
}
