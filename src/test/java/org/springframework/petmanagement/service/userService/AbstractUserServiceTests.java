package org.springframework.petmanagement.service.userService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.service.UserService;

import java.util.UUID;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractUserServiceTests {

    @Autowired
    private UserService userService;

    @BeforeEach
    public void init() {

    }

    @Test
    public void shouldAddUser() throws Exception {
        String testUsername = "testusername";
        
        Optional<User> existingUser = userService.findUserByUsername(testUsername);
        existingUser.ifPresent(user -> userService.deleteUser(user));

        User user = new User();
        user.setUsername(testUsername); 
        user.setPassword("securepassword");
        user.setEnabled(true);
        user.addRole("OWNER_ADMIN");

        userService.saveUser(user);
        
        Optional<User> fetchedUser = userService.findUserByUsername(testUsername);
        
        assertTrue(fetchedUser.isPresent());
        assertThat(fetchedUser.get().getId(), is(notNullValue()));
        assertThat(fetchedUser.get().getRoles().parallelStream().allMatch(role -> role.getRole().startsWith("ROLE_")), is(true));
        assertThat(fetchedUser.get().getRoles().parallelStream().allMatch(role -> role.getUser() != null), is(true));
    }
}