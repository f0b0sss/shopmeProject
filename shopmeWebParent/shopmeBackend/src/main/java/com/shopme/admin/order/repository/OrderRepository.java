package com.shopme.admin.order.repository;

import com.shopme.common.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE CONCAT('#', o.id) LIKE %?1% OR "
            + " CONCAT(o.firstname, ' ', o.lastname) LIKE %?1% OR"
            + " o.firstname LIKE %?1% OR"
            + " o.lastname LIKE %?1% OR o.phoneNumber LIKE %?1% OR"
            + " o.addressLine1 LIKE %?1% OR o.addressLine2 LIKE %?1% OR"
            + " o.postalCode LIKE %?1% OR o.city LIKE %?1% OR"
            + " o.state LIKE %?1% OR o.country LIKE %?1% OR"
            + " o.paymentMethod LIKE %?1% OR o.status LIKE %?1% OR"
            + " o.customer.firstname LIKE %?1% OR"
            + " o.customer.lastname LIKE %?1%")
    Page<Order> findAll(String keyword, Pageable pageable);

    Long countById(Long id);

    @Query("SELECT (o.id, o.orderTime, o.productCost,"
            + " o.subtotal, o.total) FROM Order o WHERE"
            + " o.orderTime BETWEEN ?1 AND ?2 ORDER BY o.orderTime ASC")
    List<Order> findByOrderTimeBetween(Date startTime, Date endTime);
}
