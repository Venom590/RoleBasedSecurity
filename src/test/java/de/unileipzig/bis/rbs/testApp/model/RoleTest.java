package de.unileipzig.bis.rbs.testApp.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Role test - particularly for the traversing methods
 *
 * @author Lukas Werner
 */
public class RoleTest {

    private Role root;
    private Role node1;
    private Role node2;
    private Role node3;
    private Role node4;
    private Role node5;
    private Role node6;
    private Set<Role> roles;

    private DataObject testObject;

    @Before
    public void setUp() {
        root = new Role("root", null);
        node1 = new Role("node1", root);
        node2 = new Role("node2", root);
        node3 = new Role("node3", node1);
        node4 = new Role("node4", node1);
        node5 = new Role("node5", node2);
        node6 = new Role("node6", node2);
        root.setChildRoles(new HashSet<>(Arrays.asList(node1, node2)));
        node1.setChildRoles(new HashSet<>(Arrays.asList(node3, node4)));
        node2.setChildRoles(new HashSet<>(Arrays.asList(node5, node6)));
        roles = new HashSet<>(Arrays.asList(root, node1, node2, node3, node4, node5, node6));

        testObject = new DataObject();
        RoleObject testRoleObject = new RoleObject();
        testRoleObject.setObject(testObject);
        testRoleObject.setRole(root);
        testRoleObject.setCanRead(true);
        testRoleObject.setCanWrite(true);
        testRoleObject.setCanDelete(true);
        root.setRoleObjects(new HashSet<>(Arrays.asList(testRoleObject)));
        testObject.setRoleObjects(new HashSet<>(Arrays.asList(testRoleObject)));
    }

    @Test
    public void findRoot() throws Exception {
        for (Role r: roles) {
            assertNotNull(r.findRoot());
            assertSame(r.findRoot(), root);
        }
    }

    @Test
    public void findDescendants() throws Exception {
        assertEquals(root.findDescendants(), roles);
        assertEquals(node1.findDescendants(), new HashSet<>(Arrays.asList(node3, node4)));
        assertEquals(node2.findDescendants(), new HashSet<>(Arrays.asList(node5, node6)));
        assertEquals(node3.findDescendants(), new HashSet<>());
        assertEquals(node4.findDescendants(), new HashSet<>());
        assertEquals(node5.findDescendants(), new HashSet<>());
        assertEquals(node6.findDescendants(), new HashSet<>());
    }

    @Test
    public void findAscendants() throws Exception {
        assertEquals(root.findAscendants(), new HashSet<>(Arrays.asList()));
        assertEquals(node1.findAscendants(), new HashSet<>(Arrays.asList(root)));
        assertEquals(node2.findAscendants(), new HashSet<>(Arrays.asList(root)));
        assertEquals(node3.findAscendants(), new HashSet<>(Arrays.asList(root, node1)));
        assertEquals(node4.findAscendants(), new HashSet<>(Arrays.asList(root, node1)));
        assertEquals(node5.findAscendants(), new HashSet<>(Arrays.asList(root, node2)));
        assertEquals(node6.findAscendants(), new HashSet<>(Arrays.asList(root, node2)));
    }

    @Test
    public void canRead() {
        for (Role r: roles) {
            assertTrue(r.canRead(testObject));
        }
    }

    @Test
    public void canWrite() {
        for (Role r: roles) {
            assertTrue(r.canWrite(testObject));
        }
    }

    @Test
    public void canDelete() {
        for (Role r: roles) {
            assertTrue(r.canDelete(testObject));
        }
    }

}