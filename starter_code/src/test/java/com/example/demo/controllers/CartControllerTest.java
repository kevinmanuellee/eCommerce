package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    private Cart cart = new Cart();
    private User user = new User();
    private Item item = new Item();

    @Before
    public void init(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "cartRepository", cartRepo);
        TestUtils.injectObject(cartController, "itemRepository", itemRepo);
        TestUtils.injectObject(cartController, "userRepository", userRepo);

        user.setId(0L);
        user.setUsername("kevin");
        user.setPassword("kevin123");
        user.setCart(cart);

        item.setId(0L);
        item.setName("infinity stone");
        item.setPrice(BigDecimal.valueOf(99.99));

        when(userRepo.findByUsername("kevin")).thenReturn(user);
        when(itemRepo.findById(0L)).thenReturn(Optional.of(item));
    }

    @Test
    public void test_addTocart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername(user.getUsername());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(BigDecimal.valueOf(199.98), response.getBody().getTotal());
    }

    @Test
    public void test_removeFromcart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername(user.getUsername());
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        ModifyCartRequest cartRequestModified = new ModifyCartRequest();
        cartRequestModified.setItemId(0L);
        cartRequestModified.setQuantity(1);
        cartRequestModified.setUsername(user.getUsername());
        response = cartController.removeFromcart(cartRequestModified);
        assertEquals(BigDecimal.valueOf(99.99), response.getBody().getTotal());
    }
}
