package com.shopme.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private float productCoast;
    private float shippingCoast;
    private float unitPrice;
    private float subTotal;

    @ManyToOne
    @JoinColumn(name = "produc_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getProductCoast() {
        return productCoast;
    }

    public void setProductCoast(float productCoast) {
        this.productCoast = productCoast;
    }

    public float getShippingCoast() {
        return shippingCoast;
    }

    public void setShippingCoast(float shippingCoast) {
        this.shippingCoast = shippingCoast;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
