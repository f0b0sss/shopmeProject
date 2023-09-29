package com.shopme.admin.user.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import java.util.List;

public interface UserService {
    List<User> listAll();

    void listAllByPage(int pageNum, PagingAndSortingHelper helper);

    List<Role> listRoles();

    User save(User user);

    User updateAccount(User userInForm);

    User getUserByEmail(String email);

    User getUserById(Long id) throws UserNotFoundException;

    boolean isEmailUnique(Long id, String email);

    void deleteById(Long id) throws UserNotFoundException;

    void updateEnabledStatus(Long id, boolean enabled);
}
