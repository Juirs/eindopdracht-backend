package com.example.eindopdrachtbackend.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

class RoleEqualsHashCodeTest {

    private RoleKey roleKey1;
    private RoleKey roleKey2;
    private Role role1;
    private Role role2;

    @BeforeEach
    void setUp() {
        roleKey1 = new RoleKey("john", "USER");
        roleKey2 = new RoleKey("john", "USER");
        role1 = new Role("john", "USER");
        role2 = new Role("john", "USER");
    }

    @Test
    @DisplayName("RoleKey equals() should work correctly with same data")
    void testRoleKeyEquals() {
        // Test that two RoleKey objects with same data are considered equal
        assertEquals(roleKey1, roleKey2, "RoleKey objects with same username and role should be equal");

        // Test reflexivity (object equals itself)
        assertEquals(roleKey1, roleKey1, "RoleKey should equal itself");

        // Test with null
        assertNotEquals(null, roleKey1, "RoleKey should not equal null");

        // Test with different type
        assertNotEquals("string", roleKey1, "RoleKey should not equal different type");
    }

    @Test
    @DisplayName("RoleKey hashCode() should be consistent")
    void testRoleKeyHashCode() {
        // Objects that are equal must have same hash code
        assertEquals(roleKey1.hashCode(), roleKey2.hashCode(),
            "Equal RoleKey objects must have same hash code");

        // Hash code should be consistent across multiple calls
        int firstCall = roleKey1.hashCode();
        int secondCall = roleKey1.hashCode();
        assertEquals(firstCall, secondCall,
            "Hash code should be consistent across multiple calls");
    }

    @Test
    @DisplayName("Role equals() should work correctly with same data")
    void testRoleEquals() {
        // Test that two Role objects with same data are considered equal
        assertEquals(role1, role2, "Role objects with same username and role should be equal");

        // Test reflexivity
        assertEquals(role1, role1, "Role should equal itself");

        // Test with null
        assertNotEquals(null, role1, "Role should not equal null");
    }

    @Test
    @DisplayName("Role hashCode() should be consistent")
    void testRoleHashCode() {
        // Objects that are equal must have same hash code
        assertEquals(role1.hashCode(), role2.hashCode(),
            "Equal Role objects must have same hash code");

        // Hash code should be consistent
        int firstCall = role1.hashCode();
        int secondCall = role1.hashCode();
        assertEquals(firstCall, secondCall,
            "Hash code should be consistent across multiple calls");
    }

    @Test
    @DisplayName("Set operations should work correctly with proper equals/hashCode")
    void testSetOperationsWithRoles() {
        Set<Role> roles = new HashSet<>();

        // Add first role
        roles.add(role1);
        assertEquals(1, roles.size(), "Set should contain 1 role after adding first role");

        // Add "duplicate" role (same data, different object)
        roles.add(role2);
        assertEquals(1, roles.size(), "Set should still contain 1 role after adding duplicate");

        // Test contains
        assertTrue(roles.contains(role1), "Set should contain role1");
        assertTrue(roles.contains(role2), "Set should contain role2 (same data)");

        // Test contains with new object having same data
        Role searchRole = new Role("john", "USER");
        assertTrue(roles.contains(searchRole),
            "Set should contain role with same data even if different object");
    }

    @Test
    @DisplayName("Set operations should work correctly with proper equals/hashCode for RoleKey")
    void testSetOperationsWithRoleKeys() {
        Set<RoleKey> roleKeys = new HashSet<>();

        // Add first role key
        roleKeys.add(roleKey1);
        assertEquals(1, roleKeys.size(), "Set should contain 1 role key");

        // Add "duplicate" role key
        roleKeys.add(roleKey2);
        assertEquals(1, roleKeys.size(), "Set should still contain 1 role key after adding duplicate");

        // Test contains
        assertTrue(roleKeys.contains(roleKey1), "Set should contain roleKey1");
        assertTrue(roleKeys.contains(roleKey2), "Set should contain roleKey2 (same data)");
    }

    @Test
    @DisplayName("Demonstrate what would happen WITHOUT proper equals/hashCode")
    void demonstrateProblemsWithoutEqualsHashCode() {
        // This test simulates what would happen if we used Object.equals() (reference equality)

        // Object.equals() only returns true for same reference
        assertNotEquals(System.identityHashCode(role1), System.identityHashCode(role2),
            "Different objects have different identity hash codes");

        // With proper equals(), they should be equal despite different references
        assertEquals(role1, role2, "Objects with same data should be equal with custom equals()");

        // Without proper equals(), Set would contain duplicates
        Set<Role> problematicSet = new HashSet<>();

        // If equals() was not overridden, this would result in 2 items in the set
        // But with proper implementation, it should be 1
        problematicSet.add(role1);
        problematicSet.add(role2);

        assertEquals(1, problematicSet.size(),
            "With proper equals/hashCode, Set should prevent duplicates");
    }

    @Test
    @DisplayName("Test role removal from Set")
    void testRoleRemovalFromSet() {
        Set<Role> roles = new HashSet<>();
        roles.add(role1);

        // Should be able to remove using different object with same data
        Role roleToRemove = new Role("john", "USER");
        boolean removed = roles.remove(roleToRemove);

        assertTrue(removed, "Should be able to remove role using object with same data");
        assertEquals(0, roles.size(), "Set should be empty after removal");
    }

    @Test
    @DisplayName("Test different roles are not equal")
    void testDifferentRolesNotEqual() {
        Role adminRole = new Role("john", "ADMIN");
        Role userRole = new Role("john", "USER");

        assertNotEquals(adminRole, userRole, "Roles with different role names should not be equal");

        Role differentUserRole = new Role("jane", "USER");
        assertNotEquals(userRole, differentUserRole, "Roles with different usernames should not be equal");
    }
}
