package com.shopme.shoppingCart.service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ShoppingCartException;
import com.shopme.product.repository.ProductRepository;
import com.shopme.shoppingCart.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Integer addProduct(Long productId, Integer quantity, Customer customer) throws ShoppingCartException {
        Integer updatedQuantity = quantity;
        Product product = new Product(productId);

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);

        if (cartItem != null){
            updatedQuantity = cartItem.getQuantity() + quantity;

            if (updatedQuantity > 5){
                throw new ShoppingCartException("Could not add more" + quantity + " item(s). " +
                        "Because there's already " + cartItem.getQuantity() + " items(s) " +
                        "in your shopping cart. Maximum allowed quantity is 5");
            }
        }else {
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }

        cartItem.setQuantity(updatedQuantity);

        cartItemRepository.save(cartItem);

        return updatedQuantity;
    }

    @Override
    public List<CartItem> cartItemList(Customer customer){
        return cartItemRepository.findByCustomer(customer);
    }

    @Override
    public float updateQuantity(Long productId, Integer quantity, Customer customer){
        cartItemRepository.updateQuantity(quantity, customer.getId(), productId);

        Product product = productRepository.findById(productId).get();

        float subtotal = product.getDiscountPrice() * quantity;

        return subtotal;
    }

    @Override
    public void removeProduct(Long productId, Customer customer){
        cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productId);
    }
}
