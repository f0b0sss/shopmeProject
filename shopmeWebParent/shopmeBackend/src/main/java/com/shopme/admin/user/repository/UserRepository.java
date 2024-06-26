package com.shopme.admin.user.repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends SearchRepository<User, Long> {

    User getUserByEmail(String email);

    Long countById(Long id);

    @Query("update User u set u.enabled = ?2 where u.id = ?1")
    @Modifying
    void updateEnabledStatus(Long id, boolean enabled);

    @Query("select u from User u where concat(u.id, ' ', u.email, ' ', u.firstname, ' ', u.lastname) like %?1%")
    Page<User> findAllByKeyword(String keyword, Pageable pageable);

}
