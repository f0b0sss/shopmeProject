package com.shopme.admin.setting.state.repository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Integer> {

    List<State> findByCountryOrderByNameAsc(Country country);

}
