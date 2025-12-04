package org.springframework.petmanagement.service.userService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public abstract class AbstractUserServiceTests {

    @Autowired
    protected UserService userService;

    @Test
    public void shouldAddUser() throws Exception {
        String testUsername = "u_" + System.currentTimeMillis(); 
        
        User user = new User();
        user.setUsername(testUsername);
        user.setPassword("securepassword");
        user.setEnabled(true);
        user.addRole("OWNER_ADMIN");

        userService.saveUser(user);
        
        Optional<User> fetchedUserOpt = userService.findUserByUsername(testUsername);
        
        assertThat(fetchedUserOpt).isPresent();
        
        User fetchedUser = fetchedUserOpt.get();
        
        assertThat(fetchedUser.getId()).isNotNull();
        assertThat(fetchedUser.getUsername()).isEqualTo(testUsername);
        
        assertThat(fetchedUser.getRoles()).isNotEmpty();
        assertThat(fetchedUser.getRoles())
            .allMatch(role -> role.getRole().startsWith("ROLE_"))
            .allMatch(role -> role.getUser() != null);
    }
}