package com.shopme.product.repository;

import com.shopme.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.enabled = true and (p.category.id = ?1 or p.category.allParentsIDs like %?2%) order by p.name asc")
    Page<Product> listByCategory(Long categoryId, String categoryIdMatch, Pageable pageable);

    Product findByAlias(String alias);


    @Query(value = "SELECT * FROM products p WHERE p.enabled = TRUE AND MATCH(name, short_description, full_description) AGAINST (?1)", nativeQuery = true)
    Page<Product> search(String keyword, Pageable pageable);



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
