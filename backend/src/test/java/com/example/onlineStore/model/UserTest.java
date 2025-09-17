// UserTest.java
package com.example.onlineStore.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testUserCreation() {
        User user = new User("Diego", "Rios", "Valverde");
        assertNotNull(user);
    }
}
