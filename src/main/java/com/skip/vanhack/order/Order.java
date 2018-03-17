package com.skip.vanhack.order;

import com.skip.vanhack.customer.Customer;
import com.skip.vanhack.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private BigDecimal totalPrice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static class Builder {
        private Customer customer;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private List<OrderItem> orderItems;

        public Builder(Customer customer) {
            this.customer = customer;
            this.orderItems = new ArrayList<>();
        }

        public Builder addOrderItem(Product product, BigDecimal quantity) {
            OrderItem.Builder itemBuilder = new OrderItem.Builder(product);
            itemBuilder.withQuantity(quantity);
            this.orderItems.add(itemBuilder.build());
            return this;
        }

        public Builder addOrderItem(long productId, String productName,
                                    BigDecimal productPrice, BigDecimal quantity) {
            OrderItem.Builder itemBuilder = new OrderItem.Builder(productId, productName, productPrice);
            itemBuilder.withQuantity(quantity);
            this.orderItems.add(itemBuilder.build());
            return this;
        }

        public Builder withOrderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder withOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.setCustomer(customer);
            order.setOrderItems(orderItems);
            BigDecimal totalPrice = orderItems.stream().map(OrderItem::getItemPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalPrice(totalPrice);
            order.setOrderStatus(orderStatus);
            order.setOrderDate(orderDate);
            return order;
        }
    }
}
