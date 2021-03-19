package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    private User user = new User();
    private Item item = new Item();
    private Cart cart = new Cart();

    List<Item> list_item = new ArrayList<Item>();

    @Before
    public void init(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "orderRepository", orderRepo);
        TestUtils.injectObject(orderController, "userRepository", userRepo);

        user.setId(0L);
        user.setUsername("thor");
        user.setPassword("thorthor");
        when(userRepo.findByUsername("thor")).thenReturn(user);

        item.setId(0L);
        item.setPrice(BigDecimal.valueOf(999.9));
        item.setName("Thor Hammer");
        item.setDescription("Legendary Hammer that only can be wield by those who are worthy");
        list_item.add(item);

        cart.setId(0L);
        cart.setItems(list_item);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(999.9));
        user.setCart(cart);
    }

    @Test
    public void test_submit(){
        ResponseEntity<UserOrder> response = orderController.submit("thor");
        assertEquals(BigDecimal.valueOf(999.9), response.getBody().getTotal());
        assertEquals(1, response.getBody().getItems().size());
        assertEquals(null, response.getBody().getId());
    }
}
