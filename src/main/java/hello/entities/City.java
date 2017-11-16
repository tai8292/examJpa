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
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date create_date;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modified_date;

    public City() {
    }

    public City(String name, String code, Country country, Date create_date, Date modified_date) {
        this.name = name;
        this.code = code;
        this.country = country;
        this.create_date = create_date;
        this.modified_date = modified_date;
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

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getModified_date() {
        return modified_date;
    }

    public void setModified_date(Date modified_date) {
        this.modified_date = modified_date;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", country=" + country +
                ", create_date=" + create_date +
                ", modified_date=" + modified_date +
                '}';
    }
}
