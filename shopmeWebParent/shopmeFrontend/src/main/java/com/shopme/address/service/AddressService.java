package com.shopme.address.service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

import java.util.List;

public interface AddressService {

    List<Address> listAddressBook(Customer customer);

    void save(Address address);

    Address get(Long addressId, Long customerId);

    void delete(Long addressId, Long customerId);

    void setDefaultAddress(Long defaultAddressId, Long customerId);
}
