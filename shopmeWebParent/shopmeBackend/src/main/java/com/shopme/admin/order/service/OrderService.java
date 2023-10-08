package com.shopme.admin.order.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;
import com.shopme.common.exception.OrderNotFoundException;

import java.util.List;

public interface OrderService {
    void listByPage(int pageNum, PagingAndSortingHelper helper);

    Order get(Long id) throws OrderNotFoundException;

    void delete(Long id) throws OrderNotFoundException;

    List<Country> listAllCountries();

    void save(Order orderInForm);

    void updateStatus(Long orderId, String status);
}
