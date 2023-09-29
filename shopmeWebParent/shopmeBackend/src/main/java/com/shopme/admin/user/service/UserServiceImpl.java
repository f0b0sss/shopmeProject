package com.shopme.admin.user.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.admin.user.repository.RoleRepository;
import com.shopme.admin.user.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService{
    public static final int USERS_PER_PAGE = 4;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> listAll(){
        return userRepository.findAll(Sort.by("firstname").ascending());
    }

    @Override
    public void listAllByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, USERS_PER_PAGE, userRepository);
    }

    @Override
    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    @Override
    public User save(User user) {
        boolean isUpdating = (user.getId() != null);

        if (isUpdating){
            User existingUser = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else {
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }


        return userRepository.save(user);
    }

    @Override
    public User updateAccount(User userInForm) {
        User userInDB = userRepository.findById(userInForm.getId()).get();

        if (!userInForm.getPassword().isEmpty()){
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }

        if (userInForm.getPhotos() != null){
            userInDB.setPhotos(userInForm.getPhotos());
        }

        userInDB.setFirstname(userInForm.getFirstname());
        userInDB.setLastname(userInForm.getLastname());

        return userRepository.save(userInDB);
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Long id, String email){
        User user = userRepository.getUserByEmail(email);

        if (user == null) return true;

        boolean isCreatingNew = (id == null);

        if (isCreatingNew){
            if (user != null) return false;
        }else {
            if (user.getId() != id){
                return false;
            }
        }

        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        }catch (NoSuchElementException e){
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    @Override
    public void deleteById(Long id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);

        if (countById == null || countById == 0){
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }

        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateEnabledStatus(Long id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }
}
