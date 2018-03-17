package com.skip.vanhack.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skip.vanhack.VanhackApplication;
import com.skip.vanhack.customer.Customer;
import com.skip.vanhack.customer.CustomerRepository;
import com.skip.vanhack.product.Product;
import com.skip.vanhack.product.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VanhackApplication.class)
@WebAppConfiguration
public class OrderControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Customer customer;

    private List<Product> productList = new ArrayList<>();

    private List<Order> orderList = new ArrayList<>();

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        this.orderRepository.deleteAllInBatch();

        customer = customerRepository.save(new Customer("Customer Name", "customer@email.com", "customerPassword"));

        productList.add(productRepository.save(new Product("Product 1", "Product description 1",
                new BigDecimal("9.99"), true)));
        productList.add(productRepository.save(new Product("Product 2", "Product description 2",
                new BigDecimal("19.99"), true)));

        orderList.add(orderRepository.save(new Order.Builder(customer)
                .addOrderItem(productList.get(0), BigDecimal.ONE)
                .withOrderDate(LocalDateTime.now())
                .withOrderStatus(OrderStatus.CREATED)
                .build()));
        orderList.add(orderRepository.save(new Order.Builder(customer)
                .addOrderItem(productList.get(0), BigDecimal.ONE)
                .addOrderItem(productList.get(0), new BigDecimal("3"))
                .withOrderDate(LocalDateTime.now())
                .withOrderStatus(OrderStatus.CREATED)
                .build()));
    }

    @Test
    public void shouldCreateOrderWithOneProduct() throws Exception {
        Order order = new Order.Builder(customer).addOrderItem(productList.get(0), BigDecimal.ONE).build();

        mockMvc.perform(post("/orders")
                .content(asJsonString(order))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCancelAnOrder() throws Exception {
        mockMvc.perform(delete("/orders/" + orderList.get(0).getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetAnOrderById() throws Exception {
        mockMvc.perform(get("/orders/" + orderList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void shouldGetOrderStatus() throws Exception {
        mockMvc.perform(get("/orders/" + orderList.get(0).getId() + "/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"CREATED\""));
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
