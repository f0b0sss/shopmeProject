package com.shopme.shoppingCart.repository;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCustomer(Customer customer);

    CartItem findByCustomerAndProduct(Customer customer, Product product);

    @Query("update CartItem c set c.quantity = ?1 where c.customer.id = ?2 and c.product.id = ?3")
    @Modifying
    @Transactional
    void updateQuantity(Integer quantity, Long customerId, Long productId);

    @Query("delete from CartItem c where c.customer.id = ?1 and c.product.id = ?2")
    @Modifying
    @Transactional
    void deleteByCustomerAndProduct(Long customerId, Long productId);

    @Query("delete from CartItem c where c.customer.id = ?1")
    @Modifying
    @Transactional
    void deleteByCustomer(Long customerId);


}
