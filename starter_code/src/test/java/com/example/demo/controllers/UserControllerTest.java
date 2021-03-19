package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private CartRepository cartRepo = mock(CartRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    private User user = new User();

    @Before
    public void init(){
        userController = new UserController();
        TestUtils.injectObject(userController, "cartRepository", cartRepo);
        TestUtils.injectObject(userController, "userRepository", userRepo);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", passwordEncoder);

        user.setId(0);
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.of(user));
    }

    @Test
    public void test_create_user(){

        when(passwordEncoder.encode("superPassword")).thenReturn("superHashedPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("superman");
        createUserRequest.setPassword("superPassword");
        createUserRequest.setConfirmPassword("superPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.getId());
        assertEquals("superman", responseBody.getUsername());
        assertEquals("superHashedPassword", responseBody.getPassword());
    }


    @Test
    public void test_findById() {
        ResponseEntity<User> userResponseEntity = userController.findById(0L);
        assertEquals(0, userResponseEntity.getBody().getId());
        userResponseEntity = userController.findById(1L);
        assertEquals(404, userResponseEntity.getStatusCodeValue());
    }

    @Test
    public void test_SignUp_Verification() {
        // test if user with password less than 8 can be created
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("notSuperMan");
        userRequest.setPassword("s");
        userRequest.setConfirmPassword("s");
        ResponseEntity<User> userResponseEntity = userController.createUser(userRequest);
        assertEquals(400, userResponseEntity.getStatusCodeValue());

        //test if user with unmatched password can be created
        userRequest.setUsername("heroWithNoCap");
        userRequest.setPassword("unnoticed");
        userRequest.setConfirmPassword("unnoticed123");
        userResponseEntity = userController.createUser(userRequest);
        assertEquals(400, userResponseEntity.getStatusCodeValue());
    }

}
