package com.shopme.admin.user.service;

import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<User> listAll();

    Page<User> listAllByPage(int pageNum, String sortField, String sortDir, String keyword);

    List<Role> listRoles();

    User save(User user);

    User updateAccount(User userInForm);

    User getUserByEmail(String email);

    User getUserById(Long id) throws UserNotFoundException;

    boolean isEmailUnique(Long id, String email);

    void deleteById(Long id) throws UserNotFoundException;

    void updateEnabledStatus(Long id, boolean enabled);
}
