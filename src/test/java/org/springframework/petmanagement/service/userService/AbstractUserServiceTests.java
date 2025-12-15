package org.springframework.petmanagement.service.userService;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.type.RoleType;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;
import org.springframework.petmanagement.service.UserService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractUserServiceTests {

    @Autowired
    protected UserService userService;

    @Test
    void shouldAddUser() throws Exception {
        String uniqueId = UUID.randomUUID().toString();
        String testUsername = "u_" + uniqueId;
        String testEmail = "taro-" + uniqueId + "@example.com";

        UserRegistrationDto fields = new UserRegistrationDto();
        set(fields, "username", testUsername);
        set(fields, "password", "securepassword");
        set(fields, "firstName", "太郎");
        set(fields, "lastName", "山田");
        set(fields, "firstNameKana", "タロウ");
        set(fields, "lastNameKana", "ヤマダ");
        set(fields, "email", testEmail);
        set(fields, "telephone", "090-1111-2222");

        User created = userService.createUser(fields);

        User fetchedUser = userService.getUser(created.getId());

        assertThat(fetchedUser.getId()).isNotNull();
        assertThat(fetchedUser.getUsername()).isEqualTo(testUsername);
        assertThat(fetchedUser.getEnabled()).isTrue();

        assertThat(fetchedUser.getRoles()).isNotEmpty();
        assertThat(fetchedUser.getRoles())
            .extracting("name")
            .contains(RoleType.OWNER);

        assertThat(fetchedUser.getPassword()).isNotEqualTo("securepassword");
    }

    private static void set(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}
