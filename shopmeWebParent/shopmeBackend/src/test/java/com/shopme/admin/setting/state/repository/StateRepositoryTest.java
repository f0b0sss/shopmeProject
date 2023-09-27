package com.shopme.admin.setting.state.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class StateRepositoryTest {

//    @Autowired
//    private StateRepository stateRepository;
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    public void createCountry(){
//        Integer countryId = 2;
//
//        Country country = entityManager.find(Country.class, countryId);
//
//        stateRepository.save(new State("Cherkaska oblast", country));
//        stateRepository.save(new State("kyivska oblast", country));
//        stateRepository.save(new State("Lvivska oblast", country));
//    }

}