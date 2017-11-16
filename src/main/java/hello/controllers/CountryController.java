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

    @Autowired
    CityRepository cityRepository;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public Page<Country> getListCountry(@RequestParam int pagenum) {
        PageRequest pageRequest = new PageRequest(pagenum - 1, 10);
        return countryRepository.findAll(pageRequest);
    }



    @RequestMapping(path = "add",method = RequestMethod.POST)
    public String addCountry(@ModelAttribute("country") Country country)
    {
        country.setCreated_date(new Date());
        country.setModified_date(new Date());
        countryRepository.save(country);
        return "Save";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public String deleteCountry(@RequestParam Long id) {
        if(countryRepository.exists(id))
        {
            if(cityRepository.findByCountryId(id)!=null)
            {
                return "Country dang duoc su dung";
            }
            else
            {
                countryRepository.delete(id);
                return "delete";
            }
        }
        else
        {
            return "country khong ton tai";
        }
    }

}
