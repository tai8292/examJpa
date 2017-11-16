package hello.controllers;

import hello.entities.City;
import hello.repositories.CityRepository;
import hello.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "/city")
public class CityController {

    @Autowired
    CityRepository cityRepository;

    @Autowired
    CountryRepository countryRepository;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public Page<City> getListCity(@RequestParam int pagenum) {
        PageRequest pageRequest = new PageRequest(pagenum - 1, 10);
        return cityRepository.findAll(pageRequest);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addCity(@ModelAttribute("code") String code, @ModelAttribute("name") String name,
                             @ModelAttribute("country_id") Long country_id) {
        City city = new City();
        city.setCode(code);
        city.setName(name);
        city.setCountry(countryRepository.findOne(country_id));
        city.setCreate_date(new Date());
        city.setModified_date(new Date());
        cityRepository.save(city);
        return "Save";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public String deleteCity(@RequestParam Long id)
    {
        try {
            cityRepository.delete(id);
            return "delete";
        }
        catch (Exception e)
        {
            return ""+e;
        }
    }
}
