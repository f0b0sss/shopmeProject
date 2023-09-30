package com.shopme.setting.controller;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.dto.StateDTO;
import com.shopme.setting.service.CountryService;
import com.shopme.setting.state.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryRestController {
    @Autowired private CountryService countryService;
    @Autowired private StateService stateService;


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
