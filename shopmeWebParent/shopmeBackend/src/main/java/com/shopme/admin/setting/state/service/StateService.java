package com.shopme.admin.setting.state.service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

import java.util.List;
import java.util.Optional;

public interface StateService {
    List<State> findByCountryOrderByNameAsc(Country country);

    State save(State state);

    Optional<State> findById(Integer stateId);

    void deleteById(Integer id);
}
