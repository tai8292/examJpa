package hello.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String code;
    @ManyToOne
    private Country country;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date modifiedDate;

    public City() {
    }

    public City(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(String name, String code, Country country, Date createdDate, Date modifiedDate) {
        this.name = name;
        this.code = code;
        this.country = country;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", country=" + country +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
