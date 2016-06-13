package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.TestApp;
import de.unileipzig.bis.rbs.testApp.service.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import java.security.Principal;


import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApp.class)
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mockMvc;
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "admin")
    public void users() throws Exception {
        mockMvc.perform(get("/manage/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/all-users"));

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "admin")
    public void user() throws Exception {
        int id = 2;
        mockMvc.perform(get("/manage/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user"))
                .andExpect(model().attribute("user", hasProperty("id", is(Long.valueOf(id)))))
                .andExpect(model().attribute("user", hasProperty("name", is("Test User"))));

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "admin")
    public void doEdit() throws Exception {
        int id = 2;
        String[] roleIds;
        roleIds = new String[1];
        roleIds[0] = "2L";
        mockMvc.perform(post("/manage/user/edit/{id}", id)
            .param("username", "testuser")
            .param("password", "test")
            .param("name", "New Name")
            .param("roles[]", roleIds))
                .andExpect(status().isOk())
                .andExpect(view().name("/manage/user/" + id))
                .andExpect(model().attribute("user", hasProperty("id", is(Long.valueOf(id)))))
                .andExpect(model().attribute("user", hasProperty("name", is("New Name"))));
//                .andDo(MockMvcResultHandlers.print())

    }

}