package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.UserDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }


    @Test
    void testGetUserByIdNotExists() {
        User result = userService.getUserById("nonexistent");

        assertNull(result);
    }

    @Test
    void testGetUserByEmailNotExists() {
        User result = userService.getUserByEmail("nonexistent@test.com");

        assertNull(result);
    }

    @Test
    void testUpdateUserReturnsNull() {
        UserDTO userDTO = new UserDTO();

        User result = userService.updateUser("anyid", userDTO);

        assertNull(result);
    }

    @Test
    void testDeleteUserNotExists() {
        boolean result = userService.deleteUser("nonexistent");

        assertFalse(result);
    }

    @Test
    void testGetAllUsersEmpty() {
        var result = userService.getAllUsers();

        assertEquals(0, result.size());
    }

    @Test
    void testGetUsersByRoleEmpty() {
        var result = userService.getUsersByRole("Administrator");

        assertEquals(0, result.size());
    }



}