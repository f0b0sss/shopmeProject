package com.shopme.admin.brand.repository;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Brand findByName(String name);
    Long countById(Long id);

    @Query("select b from Brand b where b.name like %?1%")
    Page<Brand> findAllByKeyword(String keyword, Pageable pageable);

    @Query("select NEW Brand(b.id, b.name) from Brand b order by b.name asc")
    List<Brand> findAll();

}
