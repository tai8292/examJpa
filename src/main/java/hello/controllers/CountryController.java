package hello.controllers;


        import hello.dto.CountryDto;
        import hello.entities.City;
        import hello.entities.Country;
        import hello.repositories.CityRepository;
        import hello.repositories.CountryRepository;
        import org.modelmapper.ModelMapper;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.data.domain.Sort;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.MediaType;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import javax.validation.Valid;
        import java.net.URI;
        import java.net.URISyntaxException;
        import java.util.Date;
        import java.util.List;

@RestController
@RequestMapping(path = "/country")
public class CountryController {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Country>> getListCountry(@RequestParam int pagenum) {
        //    PageRequest pageRequest = new PageRequest(pagenum - 1, 10, Sort.Direction.ASC,"code");
        return ResponseEntity.ok(countryRepository.findAll());
    }


    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseEntity<Country> addCountry(@RequestBody Country country) {
        Country country1 = countryRepository.save(country);
        return ResponseEntity.status(HttpStatus.CREATED).body(country1);

    }

    @RequestMapping(path = "/find", method = RequestMethod.GET)
    public ResponseEntity<Country> getCountry(@RequestParam Long id) {
        if (countryRepository.exists(id)) {
            return ResponseEntity.ok(countryRepository.findOne(id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Country> deleteCountry(@RequestParam Long id) {
        if (countryRepository.exists(id)) {
            if (cityRepository.findByCountryId(id) != null) {
                countryRepository.delete(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
