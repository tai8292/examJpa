package hello.dto;

public class CountCity {
    private Long id;
    private Long numberCity;

    public CountCity() {
    }

    public CountCity(Long id,Long numberCity) {
        this.id = id;
        this.numberCity = numberCity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumberCity() {
        return numberCity;
    }

    public void setNumberCity(Long numberCity) {
        this.numberCity = numberCity;
    }
}
