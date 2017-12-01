package hello.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String businessLicense;
    @ManyToOne
    private City city;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date modifiedDate;

    public Company() {
    }

    public Company(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Company(Long id, String name, Long city_id, String city_name) {
        System.out.println("id: " + id);
        this.id = id;
        this.name = name;
        this.city = new City();
        this.city.setId(city_id);
        this.city.setName(city_name);
    }

    public Company(String name, String businessLicense, City city, Date createDate, Date modifiedDate) {
        this.name = name;
        this.businessLicense = businessLicense;
        this.city = city;
        this.createDate = createDate;
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

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", businessLicense='" + businessLicense + '\'' +
                ", city=" + city +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
