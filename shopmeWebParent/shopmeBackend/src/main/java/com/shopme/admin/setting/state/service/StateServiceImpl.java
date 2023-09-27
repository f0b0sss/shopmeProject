package com.shopme.admin.setting.state.service;

import com.shopme.admin.setting.state.repository.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {
    @Autowired
    private StateRepository stateRepository;

    @Override
    public List<State> findByCountryOrderByNameAsc(Country country) {
        return stateRepository.findByCountryOrderByNameAsc(country);
    }

    @Override
    public State save(State state) {
        return stateRepository.save(state);
    }

    @Override
    public Optional<State> findById(Integer stateId) {
        return stateRepository.findById(stateId);
    }

    @Override
    public void deleteById(Integer id) {
        stateRepository.deleteById(id);
    }
}
