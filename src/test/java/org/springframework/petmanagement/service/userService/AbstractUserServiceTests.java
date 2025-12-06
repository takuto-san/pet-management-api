package org.springframework.petmanagement.service.userService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.UserFieldsDto;
import org.springframework.petmanagement.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public abstract class AbstractUserServiceTests {

    @Autowired
    protected UserService userService;

    @Test
    void shouldAddUser() throws Exception {
        String testUsername = "u_" + System.currentTimeMillis();

        UserFieldsDto fields = new UserFieldsDto();
        set(fields, "username", testUsername);
        set(fields, "password", "securepassword");
        set(fields, "firstName", "太郎");
        set(fields, "lastName", "山田");
        set(fields, "firstNameKana", "タロウ");
        set(fields, "lastNameKana", "ヤマダ");
        set(fields, "email", "taro@example.com");
        set(fields, "telephone", "090-1111-2222");

        User created = userService.createUser(fields);

        Optional<User> fetchedUserOpt = userService.findById(created.getId());

        assertThat(fetchedUserOpt).isPresent();
        User fetchedUser = fetchedUserOpt.get();

        assertThat(fetchedUser.getId()).isNotNull();
        assertThat(fetchedUser.getUsername()).isEqualTo(testUsername);
        assertThat(fetchedUser.isEnabled()).isTrue();
        assertThat(fetchedUser.getRole()).isEqualTo("user");
        assertThat(fetchedUser.getPassword()).isNotEqualTo("securepassword");
    }

    private static void set(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}