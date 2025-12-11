package org.springframework.petmanagement.service.userService;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.UserFieldsDto;
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

        UserFieldsDto fields = new UserFieldsDto();
        set(fields, "username", testUsername);
        set(fields, "password", "securepassword");
        set(fields, "firstName", "太郎");
        set(fields, "lastName", "山田");
        set(fields, "firstNameKana", "タロウ");
        set(fields, "lastNameKana", "ヤマダ");
        set(fields, "email", testEmail);
        set(fields, "telephone", "090-1111-2222");

        User created = userService.createUser(fields);

        Optional<User> fetchedUserOpt = userService.findById(created.getId());

        assertThat(fetchedUserOpt).isPresent();
        User fetchedUser = fetchedUserOpt.get();

        assertThat(fetchedUser.getId()).isNotNull();
        assertThat(fetchedUser.getUsername()).isEqualTo(testUsername);
        assertThat(fetchedUser.getEnabled()).isTrue();
        assertThat(fetchedUser.getRole()).isEqualTo("user");
        assertThat(fetchedUser.getPassword()).isNotEqualTo("securepassword");
    }

    private static void set(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}