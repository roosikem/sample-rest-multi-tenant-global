package com.lkup.accounts.accounts.controller;

import com.lkup.accounts.accounts.config.MongoDbTestContainerExtension;
import com.lkup.accounts.accounts.config.TestDataInitializer;
import com.lkup.accounts.accounts.config.WithMockCustomUser;
import com.lkup.accounts.document.Organization;
import com.lkup.accounts.dto.team.CreateTeamDto;
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
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String countryId;

    @DynamicPropertySource
    static void setMongoProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.data.mongodb.uri",  MongoDbTestContainerExtension::getReplicaSetUrl);
    }


    @Test
    @WithMockCustomUser
    @WithMockUser
    public void createTeamTest() throws Exception {
        CreateTeamDto createTeamDto = new CreateTeamDto();
        createTeamDto.setName("Test Team");
        Map<String, Organization> organizationMap = (Map<String, Organization>) TestDataInitializer
                    .getDataObjectbyName(TestDataInitializer.ORGANIZATION_NAME_MAP);
        createTeamDto.setOrganization(organizationMap.get(TestDataInitializer.VF_1).getId());

        String requestBody  =  objectMapper.writeValueAsString(createTeamDto);
        MvcResult  mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
