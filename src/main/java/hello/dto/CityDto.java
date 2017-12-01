package hello.dto;

import javax.validation.constraints.Size;
import java.util.Date;

public class CityDto {

    private Long id;
    private String code;
    private String name;
    private Date createDate;
    private Date modifiedDate;
    private CountryDto countryDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public CountryDto getCountryDto() {
        return countryDto;
    }

    public void setCountryDto(CountryDto countryDto) {
        this.countryDto = countryDto;
    }

    @Override
    public String toString() {
        return "CityDto{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                ", countryDto=" + countryDto +
                '}';
    }
}
