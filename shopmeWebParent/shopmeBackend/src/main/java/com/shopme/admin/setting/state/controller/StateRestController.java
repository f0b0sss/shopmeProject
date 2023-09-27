package com.shopme.admin.setting.state.controller;

import com.shopme.admin.setting.state.service.StateService;
import com.shopme.common.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/states")
public class StateRestController {
    @Autowired private StateService stateService;


    @PostMapping("/save")
    public String save(@RequestBody State state){
        State savedState = stateService.save(state);
        return String.valueOf(savedState.getId());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        stateService.deleteById(id);
    }

}
