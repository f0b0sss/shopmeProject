package com.shopme.admin.user.repository;

import com.shopme.admin.user.repository.RoleRepository;
import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    public void createFirstRoleTest(){
        Role roleAdmin = new Role("Admin", "Manage everything");
        Role savedRole = repository.save(roleAdmin);

        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void createRestRoleTest(){
        Role roleSalesPerson = new Role("SalesPerson", "Manage product price, customers, " +
                "shipping, orders and sales report");
        Role roleEditor = new Role("Editor", "Manage categories, brands, products, articles and menus");
        Role roleShipper = new Role("Shipper", "View products, orders and update order status");
        Role roleAssistant = new Role("Asistant", "Manage questions and reviews");

        repository.saveAll(List.of(roleSalesPerson, roleEditor, roleShipper, roleAssistant));
    }

}