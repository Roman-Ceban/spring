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


    //    public Contacts(Long id, Long phone, String website) {
//        this.id = id;
//        this.phone = phone;
//        this.website = website;
//    }
    public Contacts() {
    }
    public Contacts(Long s10, String s11) {
        this.phone = s10;
        this.website = s11;
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



}
