package com.lkup.accounts.accounts.config;

import com.lkup.accounts.document.*;
import com.lkup.accounts.dto.organization.CreateOrganizationDto;
import com.lkup.accounts.mapper.OrganizationMapper;
import com.lkup.accounts.mapper.TeamMapper;
import com.lkup.accounts.repository.global.*;
import com.lkup.accounts.service.DefaultUUIDGeneratorService;
import com.lkup.accounts.utilities.RoleConstants;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.lkup.accounts.utilities.PermissionConstants.*;

@TestConfiguration
public class TestDataInitializer {

    public static final String ROLES_MAP = "ROLES_MAP";
    public static final String ORGANIZATION_NAME_MAP = "ORGANIZATION_NAME_MAP";
    public static final String ORGANIZATION_ID_MAP = "ORGANIZATION_ID_MAP";
    public static final String TEAMS_MAP = "TEAMS_MAP";
    public static final String USERS_MAP = "USERS_MAP";
    public static final String TEST_USER = "test-user";
    public static final String TEST_USER_2 = "test-user-2";
    public static final String TEST_USER_3 = "test-user-3";
    public static final String TEST_TEAM_1 = "Test-Team1";
    public static final String TEST_TEAM_2 = "Test-Team2";
    public static final String TEST_TEAM_3 = "Test-Team3";
    public static final String TEST_TEAM_4 = "Test-Team4";
    public static final String VF_1 = "VF-1";
    public static final String VF_2 = "VF-2";

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private DefaultUUIDGeneratorService defaultUUIDGenerator;

    private static boolean initialized = false;

    public static final String COUNTRIES = "countries";
    private static final Map<String, Object> DATA = new HashMap<>();

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    OrganizationMapper organizationMapper;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamMapper teamMapper;

    @PostConstruct
    public void initializeData() {
        if (!initialized) {
            // Clear previous test data
            countryRepository.deleteAll();

            // Insert test countries
            Country country1 = new Country(defaultUUIDGenerator.generateId(), "Country 1", "C1");
            Country country2 = new Country(defaultUUIDGenerator.generateId(), "Country 2", "C2");
            Country country3 = new Country(defaultUUIDGenerator.generateId(), "Country 3", "C3");

            countryRepository.save(country1);
            countryRepository.save(country2);
            countryRepository.save(country3);

            Map<String, Country> countries = new HashMap<>();
            countries.put("C1", country1);
            countries.put("C2", country2);
            countries.put("C3", country3);
            DATA.put(COUNTRIES, countries);

            Map<String, Role> rolesMap = new HashMap<>();
            roleRepository.deleteAll();
            Role role = new Role();
            role.setId(defaultUUIDGenerator.generateId());
            role.setName(RoleConstants.SUPER_ADMIN_ROLE);
            role.setPermissions(Set.of(ADMINISTRATOR));
            role = roleRepository.save(role);
            rolesMap.put(role.getName(), role);

            Role role2 = new Role();
            role2.setId(defaultUUIDGenerator.generateId());
            role2.setName(RoleConstants.USER_ROLE);
            role2.setPermissions(Set.of(ADMINISTRATOR));
            roleRepository.save(role2);
            rolesMap.put(role2.getName(), role2);

            Role role3 = new Role();
            role3.setId(defaultUUIDGenerator.generateId());
            role3.setName(RoleConstants.USER_ROLE);
            role3.setPermissions(Set.of(CREATE_CONFIGURATION, VIEW_CONFIGURATION, UPDATE_CONFIGURATION));
            roleRepository.save(role3);
            rolesMap.put(role3.getName(), role3);

            Map<String, Organization> organizationMap = new HashMap<>();
            Map<String, Organization> organizationNameMap = new HashMap<>();

            organizationRepository.deleteAll();
            CreateOrganizationDto organizationDto = createOrganizationDto(VF_1, country1.getId());
            Organization organizationDb1 =
                    organizationRepository.save(organizationMapper.convertCreateDtoToOrganization(organizationDto));
            organizationMap.put(organizationDb1.getId(), organizationDb1);
            organizationNameMap.put(organizationDb1.getName(), organizationDb1);


            CreateOrganizationDto organizationDto2 = createOrganizationDto(VF_2, country2.getId());
            Organization organizationDb2 =
                    organizationRepository.save(organizationMapper.convertCreateDtoToOrganization(organizationDto2));
            organizationMap.put(organizationDb2.getId(), organizationDb2);
            organizationNameMap.put(organizationDb2.getName(), organizationDb2);

            Map<String, Team> teams = new HashMap<>();

            teamRepository.deleteAll();

            Team team1 = new Team();
            team1.setId(defaultUUIDGenerator.generateId());
            team1.setOrganization(organizationDb1);
            team1.setName(TEST_TEAM_1);
            team1 = teamRepository.save(team1);
            teams.put(team1.getId(), team1);

            Team team2 = new Team();
            team2.setId(defaultUUIDGenerator.generateId());
            team2.setOrganization(organizationDb1);
            team2.setName(TEST_TEAM_2);
            team2 = teamRepository.save(team2);
            teams.put(team2.getId(), team2);

            Team team3 = new Team();
            team3.setId(defaultUUIDGenerator.generateId());
            team3.setOrganization(organizationDb2);
            team3.setName(TEST_TEAM_3);
            team3 = teamRepository.save(team3);
            teams.put(team3.getId(), team3);

            Team team4 = new Team();
            team4.setOrganization(organizationDb2);
            team4.setName(TEST_TEAM_4);
            team4 = teamRepository.save(team4);
            teams.put(team4.getId(), team4);

            Map<String, User> users = new HashMap<>();

            userRepository.deleteAll();
            User root = new User();
            root.setId(defaultUUIDGenerator.generateId());
            root.setCountryAccess(List.of(country1));
            root.setUsername(TEST_USER);
            root.setPassword(passwordEncoder.encode("password"));
            root.setOrganization(organizationDb1);
            root.setTeams(List.of(team1));
            root.setRole(role);

            root = userRepository.save(root);
            users.put(root.getUsername(), root);

            root = new User();
            root.setId(defaultUUIDGenerator.generateId());
            root.setCountryAccess(List.of(country1));
            root.setUsername(TEST_USER_2);
            root.setPassword(passwordEncoder.encode("password"));
            root.setOrganization(organizationDb2);
            root.setTeams(List.of(team2));
            root.setRole(role2);
            root = userRepository.save(root);
            users.put(root.getUsername(), root);

            root = new User();
            root.setId(defaultUUIDGenerator.generateId());
            root.setCountryAccess(List.of(country1));
            root.setUsername(TEST_USER_3);
            root.setPassword(passwordEncoder.encode("password"));
            root.setOrganization(organizationDb2);
            root.setTeams(List.of(team4));
            root.setRole(role3);
            root = userRepository.save(root);

            users.put(root.getUsername(), root);

            DATA.put(ROLES_MAP, rolesMap);
            DATA.put(ORGANIZATION_ID_MAP, organizationMap);
            DATA.put(ORGANIZATION_NAME_MAP, organizationNameMap);
            DATA.put(TEAMS_MAP, teams);
            DATA.put(USERS_MAP, users);
            initialized = true;
        }
    }

    public static Object getDataObjectbyName(String name) {
        return DATA.get(name);
    }

    private CreateOrganizationDto createOrganizationDto(String orgName, String countryId) {
        CreateOrganizationDto organizationDto = new CreateOrganizationDto();
        organizationDto.setId(defaultUUIDGenerator.generateId());
        organizationDto.setName(orgName);
        organizationDto.setCountry(countryId);
        return organizationDto;
    }
}
