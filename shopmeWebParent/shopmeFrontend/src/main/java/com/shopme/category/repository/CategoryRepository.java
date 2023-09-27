package com.shopme.category.repository;

import com.shopme.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.enabled = true order by c.name asc")
    List<Category> findAllEnabled();

    @Query("select c from Category c where c.enabled = true and c.alias = ?1")
    Category findAllByAliasEnabled(String alias);


//    @Query("select c from Category c where c.parent.id is null")
//    List<Category> findRootCategories(Sort sort);
//
//    @Query("select c from Category c where c.parent.id is null")
//    Page<Category> findRootCategories(Pageable pageable);
//
//    Category findByName(String name);
//
//    Category findByAlias(String alias);
//
//    Long countById(Long id);
//
//    @Query("update Category c set c.enabled = ?2 where c.id = ?1")
//    @Modifying
//    void updateEnabledStatus(Long id, boolean enabled);
//
//    @Query("select c from Category c where concat(c.id, ' ', c.name, ' ', c.alias) like %?1%")
//    Page<Category> findAllByKeyword(String keyword, Pageable pageable);

}
