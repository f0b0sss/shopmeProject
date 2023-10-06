package com.shopme.address.service;

import com.shopme.address.repository.AddressRepository;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> listAddressBook(Customer customer){
        return addressRepository.findByCustomer(customer);
    }

    @Override
    public void save(Address address) {
        addressRepository.save(address);
    }

    @Override
    public Address get(Long addressId, Long customerId) {
        return addressRepository.findByIdAndCustomer(addressId, customerId);
    }

    @Override
    public void delete(Long addressId, Long customerId) {
        addressRepository.deleteByIdAndCustomer(addressId, customerId);
    }

    @Override
    public void setDefaultAddress(Long defaultAddressId, Long customerId){

        if (defaultAddressId > 0){
            addressRepository.setDefaultAddress(defaultAddressId);
        }

        addressRepository.setNonDefaultForOthers(defaultAddressId, customerId);
    }

    @Override
    public Address getDefaultAddress(Customer customer){
        return addressRepository.findDefaultByCustomer(customer.getId());

    }



}
