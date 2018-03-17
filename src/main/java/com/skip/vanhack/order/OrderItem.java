package com.skip.vanhack.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skip.vanhack.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "ORDER_ITEMS")
public class OrderItem {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private long productId;

    private String productName;

    private BigDecimal productPrice;

    private BigDecimal itemQuantity;

    private BigDecimal itemPrice;

    @JsonIgnore
    @ManyToOne
    private Order order;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(BigDecimal itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static class Builder {
        private long productId;
        private String productName;
        private BigDecimal productPrice;
        private BigDecimal itemQuantity;

        public Builder(Product product) {
            this.productId = product.getId();
            this.productName = product.getName();
            this.productPrice = product.getPrice();
            this.itemQuantity = BigDecimal.ONE;
        }

        public Builder(long productId, String productName, BigDecimal productPrice) {
            this.productId = productId;
            this.productName = productName;
            this.productPrice = productPrice;
        }

        public Builder withQuantity(BigDecimal itemQuantity) {
            this.itemQuantity = itemQuantity;
            return this;
        }

        public OrderItem build() {
            OrderItem item = new OrderItem();
            item.productId = productId;
            item.productName = productName;
            item.productPrice = productPrice;
            item.itemQuantity = itemQuantity;
            item.itemPrice = itemQuantity.multiply(productPrice);
            return item;
        }
    }
}
