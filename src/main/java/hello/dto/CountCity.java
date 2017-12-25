package hello.dto;

public class CountCity {
    private Long id;
    private Long countCity;

    public CountCity() {
    }

    public CountCity(Long id,Long countCity) {
        this.id = id;
        this.countCity = countCity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCountCity() {
        return countCity;
    }

    public void setCountCity(Long countCity) {
        this.countCity = countCity;
    }
}
