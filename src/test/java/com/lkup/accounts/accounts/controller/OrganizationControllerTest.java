package com.lkup.accounts.accounts.controller;

import com.lkup.accounts.accounts.config.MongoDbTestContainerExtension;
import com.lkup.accounts.accounts.config.TestDataInitializer;
import com.lkup.accounts.accounts.config.WithMockCustomUser;
import com.lkup.accounts.document.Country;
import com.lkup.accounts.dto.organization.CreateOrganizationDto;
import com.lkup.accounts.utilities.RoleConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@SpringBootTest(classes = TestDataInitializer.class)
@ExtendWith(MongoDbTestContainerExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String countryId;

    @DynamicPropertySource
    static void setMongoProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.data.mongodb.uri",  MongoDbTestContainerExtension::getReplicaSetUrl);
    }


    @Test
    @WithMockCustomUser(username = TestDataInitializer.TEST_USER)
    public void createOrganizationTest() throws Exception {
        CreateOrganizationDto organizationDto = new CreateOrganizationDto();
        organizationDto.setName("Test Organization");
        Map<String, Country> countryMap = (Map<String, Country>) TestDataInitializer.getDataObjectbyName(TestDataInitializer.COUNTRIES);
        organizationDto.setCountry(countryMap.get("C1").getId());
        String requestBody  =  objectMapper.writeValueAsString(organizationDto);
        MvcResult  mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
       System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
