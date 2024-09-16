package com.lkup.accounts.accounts.controller;

import com.lkup.accounts.accounts.config.MongoDbTestContainerExtension;
import com.lkup.accounts.accounts.config.TestDataInitializer;
import com.lkup.accounts.accounts.config.WithMockCustomUser;
import com.lkup.accounts.context.jwt.JwtAuthenticationFilter;
import com.lkup.accounts.document.User;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = TestDataInitializer.class)
@ExtendWith(MongoDbTestContainerExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void setMongoProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.data.mongodb.uri",  MongoDbTestContainerExtension::getReplicaSetUrl);
    }

    @Test
    @DisplayName("Successfully Login")
    public void login_success() throws Exception {
        Map<String, User> userMap = (Map<String, User>) TestDataInitializer.getDataObjectbyName(TestDataInitializer.USERS_MAP);
        User principal = userMap.get(TestDataInitializer.TEST_USER);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", principal.getUsername()).param("password", "password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Un-Successfully Login")
    public void login_unsuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "invalid user").param("password", "password"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn();
    }

    @Test
    @DisplayName("Un-Successfully Fetch userinfo")
    @WithMockCustomUser(username = TestDataInitializer.TEST_USER)
    public void userInfo_unsuccessful() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn();
    }

    @Test
    @DisplayName("UserInfo Successfully Fetch")
    public void userInfo_success() throws Exception {
        Map<String, User> userMap = (Map<String, User>) TestDataInitializer.getDataObjectbyName(TestDataInitializer.USERS_MAP);
        User principal = userMap.get(TestDataInitializer.TEST_USER);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", principal.getUsername()).param("password", "password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());

        // Extract the token from the JSON response
        String token = jsonObject.getString("token");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/user")
                        .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
