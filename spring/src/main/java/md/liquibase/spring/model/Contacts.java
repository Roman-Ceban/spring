package md.liquibase.spring.model;

import javax.persistence.*;

@Entity
@Table(name = "contacts", schema = "public")
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long phone;
    private String website;

    public Contacts() {
    }

    public Contacts(Long id, Long phone, String website) {
        this.id = id;
        this.phone = phone;
        this.website = website;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
