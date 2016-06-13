package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.TestApp;
import de.unileipzig.bis.rbs.testApp.model.User;
import de.unileipzig.bis.rbs.testApp.service.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApp.class)
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "admin")
    public void test1Users() throws Exception {
        mockMvc.perform(get("/manage/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/all-users"));

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "admin")
    public void test2User() throws Exception {
        System.out.println("test2User()");
        int id = 2;
        mockMvc.perform(get("/manage/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user"))
                .andExpect(model().attribute("user", hasProperty("id", is(Long.valueOf(id)))))
                .andExpect(model().attribute("user", hasProperty("name", is("Test User"))));

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "admin")
    public void test3DoEdit() throws Exception {
        System.out.println("test3DoEdit()");
        final int id = 2;
        mockMvc.perform(post("/manage/user/edit/{id}", id)
            .param("username", "testuser")
            .param("password", "test")
            .param("name", "New Name"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/manage/user/" + id));
        User user = userRepository.findOne((long)id);
        Assert.assertEquals(user.getName(), "New Name");
    }

    public static class MockSecurityContext implements SecurityContext {

        private Authentication authentication;

        public MockSecurityContext(Authentication authentication) {
            this.authentication = authentication;
        }

        @Override
        public Authentication getAuthentication() {
            return authentication;
        }

        @Override
        public void setAuthentication(Authentication authentication) {
            this.authentication = authentication;
        }
    }

}