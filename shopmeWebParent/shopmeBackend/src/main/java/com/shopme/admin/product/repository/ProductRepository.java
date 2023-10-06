package com.shopme.admin.product.repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends SearchRepository<Product, Long> {

    @Query("select p from Product p where p.name like %?1% " +
            "or p.shortDescription like %?1%" +
            "or p.fullDescription like %?1%" +
            "or p.brand.name like %?1%" +
            "or p.category.name like %?1%")
    Page<Product> findAllByKeyword(String keyword, Pageable pageable);

    @Query("select p from Product p where p.category.id = ?1 " +
            "or p.category.allParentsIDs like %?2% ")
    Page<Product> findAllInCategory(Long categoryId, String categoryIdMatch, Pageable pageable);

    @Query("select p from Product p where (p.category.id = ?1 " +
            "or p.category.allParentsIDs like %?2% ) and " +
            "(p.name like %?3%" +
            "or p.shortDescription like %?3%" +
            "or p.fullDescription like %?3%" +
            "or p.brand.name like %?3%" +
            "or p.category.name like %?3% )")
    Page<Product> searchInCategory(Long categoryId, String categoryIdMatch, String keyword, Pageable pageable);

    Product findByName(String name);

    Long countById(Long id);

    @Query("update Product p set p.enabled = ?2 where p.id = ?1")
    @Modifying
    void updateEnabledStatus(Long id, boolean enabled);

}
