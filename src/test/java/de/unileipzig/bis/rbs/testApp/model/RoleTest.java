package de.unileipzig.bis.rbs.testApp.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    }

    @Test
    public void findRoot() throws Exception {
        for (Role r: roles) {
            Assert.assertNotNull(r.findRoot());
            Assert.assertSame(r.findRoot(), root);
        }
    }

    @Test
    public void findDescendants() throws Exception {
        Assert.assertEquals(root.findDescendants(), roles);
        Assert.assertEquals(node1.findDescendants(), new HashSet<>(Arrays.asList(node3, node4)));
        Assert.assertEquals(node2.findDescendants(), new HashSet<>(Arrays.asList(node5, node6)));
        Assert.assertEquals(node3.findDescendants(), new HashSet<>());
        Assert.assertEquals(node4.findDescendants(), new HashSet<>());
        Assert.assertEquals(node5.findDescendants(), new HashSet<>());
        Assert.assertEquals(node6.findDescendants(), new HashSet<>());
    }

    @Test
    public void findAscendants() throws Exception {
        Assert.assertEquals(root.findAscendants(), new HashSet<>(Arrays.asList()));
        Assert.assertEquals(node1.findAscendants(), new HashSet<>(Arrays.asList(root)));
        Assert.assertEquals(node2.findAscendants(), new HashSet<>(Arrays.asList(root)));
        Assert.assertEquals(node3.findAscendants(), new HashSet<>(Arrays.asList(root, node1)));
        Assert.assertEquals(node4.findAscendants(), new HashSet<>(Arrays.asList(root, node1)));
        Assert.assertEquals(node5.findAscendants(), new HashSet<>(Arrays.asList(root, node2)));
        Assert.assertEquals(node6.findAscendants(), new HashSet<>(Arrays.asList(root, node2)));
    }

}