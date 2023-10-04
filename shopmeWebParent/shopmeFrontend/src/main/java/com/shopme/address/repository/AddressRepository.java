package com.shopme.address.repository;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByCustomer(Customer customer);

    @Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    Address findByIdAndCustomer(Long addressId, Long customerId);

    @Query("DELETE FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    @Modifying
    @Transactional
    void deleteByIdAndCustomer(Long addressId, Long customerId);

    @Query("UPDATE Address a SET a.defaultForShipping = true WHERE a.id = ?1")
    @Modifying
    @Transactional
    void setDefaultAddress(Long id);

    @Query("UPDATE Address a SET a.defaultForShipping = false WHERE a.id <> ?1 AND a.customer.id = ?2")
    @Modifying
    @Transactional
    void setNonDefaultForOthers(Long defaultAddressId, Long customerId);

    @Query("SELECT a FROM Address a WHERE a.customer.id = ?1 AND a.defaultForShipping = true")
    Address findDefaultByCustomer(Long customerId);
}
