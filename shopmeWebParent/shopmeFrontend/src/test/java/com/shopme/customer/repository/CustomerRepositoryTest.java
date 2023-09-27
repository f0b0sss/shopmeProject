package com.shopme.customer.repository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void createCustomer1(){
        Integer countryId = 2;
        Integer stateId = 1;

        Country country = entityManager.find(Country.class, countryId);
        State state = entityManager.find(State.class, stateId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setState(state.getName());
        customer.setCity("Cherkasy");
        customer.setFirstname("Dmytro");
        customer.setLastname("Zimin");
        customer.setPassword("12345");
        customer.setEmail("myprivat007@gmail.com");
        customer.setPhoneNumber("380636621800");
        customer.setAddressLine1("gd69k360");
        customer.setPostalCode("18021");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void createCustomer2(){
        Integer countryId = 2;
        Integer stateId = 2;

        Country country = entityManager.find(Country.class, countryId);
        State state = entityManager.find(State.class, stateId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setState(state.getName());
        customer.setCity("Kiev");
        customer.setFirstname("Dmytro1");
        customer.setLastname("Zimin1");
        customer.setPassword("12345");
        customer.setEmail("myprivat008@gmail.com");
        customer.setPhoneNumber("380636621801");
        customer.setAddressLine1("gd69k361");
        customer.setPostalCode("18021");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }
}