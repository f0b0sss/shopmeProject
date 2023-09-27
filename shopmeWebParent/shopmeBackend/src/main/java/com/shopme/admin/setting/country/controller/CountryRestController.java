package com.shopme.admin.setting.country.controller;

import com.shopme.admin.setting.country.service.CountryService;
import com.shopme.common.entity.dto.StateDTO;
import com.shopme.admin.setting.state.service.StateService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryRestController {
    @Autowired private CountryService countryService;
    @Autowired private StateService stateService;

    @GetMapping
    public List<Country> countries(){
        return countryService.findAllByOrderByNameAsc();
    }

    @PostMapping("/save")
    public String save(@RequestBody Country country){
        Country savedCountry = countryService.save(country);
        return String.valueOf(savedCountry.getId());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        countryService.deleteById(id);
    }

    @GetMapping("/{countryId}/states")
    public List<StateDTO> listStatesByCountry(@PathVariable Integer countryId){
        List<StateDTO> result = new ArrayList<>();

        List<State> stateList = stateService.findByCountryOrderByNameAsc(new Country(countryId));

        for (State state : stateList) {
            result.add(new StateDTO(state.getId(), state.getName()));
        }

        return result;
    }

}
