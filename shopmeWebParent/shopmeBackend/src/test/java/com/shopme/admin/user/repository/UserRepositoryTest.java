package com.shopme.admin.user.repository;

import com.shopme.admin.user.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void createUserWithOneRoleTest(){
        Role roleAdmin = testEntityManager.find(Role.class, 1);

        User userZimin = new User("admin@mail.com", "admin", "Dmitriy", "Zimin");
        userZimin.addRole(roleAdmin);

        User savedUser = userRepository.save(userZimin);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void createUserWithTwoRolesTest(){
        User userLiza = new User("liza@mail.com", "liza", "Liza", "Matveykina");
        Role roleEditor = new Role(3l);
        Role roleAssistant = new Role(5l);

        userLiza.addRole(roleEditor);
        userLiza.addRole(roleAssistant);

        User savedUser = userRepository.save(userLiza);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void listAllUsersTest(){
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(System.out::println);
    }

    @Test
    public void getUserByIdTest(){
        User userZimin = userRepository.findById(1l).get();

        assertThat(userZimin).isNotNull();
    }

    @Test
    public void updateUserDetailsTest(){
        User userZimin = userRepository.findById(1l).get();
        userZimin.setEnabled(true);
        userZimin.setEmail("super@mail.com");

        userRepository.save(userZimin);
    }

    @Test
    public void updateUserRolesTest(){
        User user = userRepository.findById(2l).get();
        Role roleEditor = new Role(3l);
        Role roleSalesperson = new Role(2l);

        user.getRoles().remove(roleEditor);
//        user.addRole(roleSalesperson);

        userRepository.save(user);
    }

    @Test
    public void deleteUserTest(){
        userRepository.deleteById(2l);
    }

    @Test
    public void getUserByEmailTest(){
        String email = "admin@mail.com";

        User user = userRepository.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void countByIdTest(){
        Long id = 1l;
        Long countById = userRepository.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void disableUserTest(){
        Long id = 1l;

        userRepository.updateEnabledStatus(id, false);
    }

    @Test
    public void listFirstPageTest(){
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> users = page.getContent();

        users.forEach(user -> System.out.println(user));

        assertThat(users.size()).isEqualTo(pageSize);
    }

    @Test
    public void searchUserTest(){
        String keyword = "zimin";

        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAllByKeyword(keyword, pageable);

        List<User> users = page.getContent();

        users.forEach(user -> System.out.println(user));

        assertThat(users.size()).isGreaterThan(0);
    }


}