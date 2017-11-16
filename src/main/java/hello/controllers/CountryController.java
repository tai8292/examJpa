package hello.controllers;

import hello.entities.City;
import hello.entities.Country;
import hello.repositories.CityRepository;
import hello.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/country")
public class CountryController {

    @Autowired
    CountryRepository countryRepository;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public Page<Country> getListCountry(@RequestParam int pagenum)
    {
        PageRequest pageRequest = new PageRequest(pagenum-1,10);
        return countryRepository.findAll(pageRequest);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addCountry(@ModelAttribute("code")String code,@ModelAttribute("name")String name)
    {
        Country country = new Country();
        country.setCode(code);
        country.setName(name);
        country.setCreated_date(new Date());
        country.setModified_date(new Date());
        countryRepository.save(country);
        return "save "+country;
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public String deleteCountry(@RequestParam Long id) {
        try {
            countryRepository.delete(id);
            return "delete";
        }
        catch (Exception e)
        {
            return ""+e;
        }
    }

}
