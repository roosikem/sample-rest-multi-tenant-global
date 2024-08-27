package com.lkup.accounts;

import com.lkup.accounts.context.RequestContext;
import com.lkup.accounts.context.RequestInfo;
import com.lkup.accounts.document.Country;
import com.lkup.accounts.document.Organization;
import com.lkup.accounts.document.Role;
import com.lkup.accounts.document.User;
import com.lkup.accounts.dto.appId.AppIdDto;
import com.lkup.accounts.dto.environment.CreateEnvironmentDto;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.team.TeamDto;
import com.lkup.accounts.mapper.APPIdMapper;
import com.lkup.accounts.mapper.EnvironmentMapper;
import com.lkup.accounts.mapper.OrganizationMapper;
import com.lkup.accounts.mapper.TeamMapper;
import com.lkup.accounts.repository.global.CountryRepository;
import com.lkup.accounts.repository.global.RoleRepository;
import com.lkup.accounts.repository.global.UserRepository;
import com.lkup.accounts.repository.tenant.EnvironmentRepository;
import com.lkup.accounts.service.*;
import com.lkup.accounts.utilities.ApplicationConstants;
import com.lkup.accounts.utilities.RoleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.lkup.accounts.utilities.ApplicationConstants.*;
import static com.lkup.accounts.utilities.PermissionConstants.*;

@SpringBootApplication
@EnableMongoAuditing
public class GtpAccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GtpAccountsApplication.class, args);
    }

    static final String defaultConfig = "{\n  \"users\": [\n    {\n      \"id\": 1,\n      \"firstName\": \"Emily\",\n      \"lastName\": \"Johnson\",\n      \"maidenName\": \"Smith\",\n      \"age\": 28,\n      \"gender\": \"female\",\n      \"email\": \"emily.johnson@x.dummyjson.com\",\n      \"phone\": \"+81 965-431-3024\",\n      \"username\": \"emilys\",\n      \"password\": \"emilyspass\",\n      \"birthDate\": \"1996-5-30\",\n      \"image\": \"...\",\n      \"bloodGroup\": \"O-\",\n      \"height\": 193.24,\n      \"weight\": 63.16,\n      \"eyeColor\": \"Green\",\n      \"hair\": {\n        \"color\": \"Brown\",\n        \"type\": \"Curly\"\n      },\n      \"ip\": \"42.48.100.32\",\n      \"address\": {\n        \"address\": \"626 Main Street\",\n        \"city\": \"Phoenix\",\n        \"state\": \"Mississippi\",\n        \"stateCode\": \"MS\",\n        \"postalCode\": \"29112\",\n        \"coordinates\": {\n          \"lat\": -77.16213,\n          \"lng\": -92.084824\n        },\n        \"country\": \"United States\"\n      },\n      \"macAddress\": \"47:fa:41:18:ec:eb\",\n      \"university\": \"University of Wisconsin--Madison\",\n      \"bank\": {\n        \"cardExpire\": \"03/26\",\n        \"cardNumber\": \"9289760655481815\",\n        \"cardType\": \"Elo\",\n        \"currency\": \"CNY\",\n        \"iban\": \"YPUXISOBI7TTHPK2BR3HAIXL\"\n      },\n      \"company\": {\n        \"department\": \"Engineering\",\n        \"name\": \"Dooley, Kozey and Cronin\",\n        \"title\": \"Sales Manager\",\n        \"address\": {\n          \"address\": \"263 Tenth Street\",\n          \"city\": \"San Francisco\",\n          \"state\": \"Wisconsin\",\n          \"stateCode\": \"WI\",\n          \"postalCode\": \"37657\",\n          \"coordinates\": {\n            \"lat\": 71.814525,\n            \"lng\": -161.150263\n          },\n          \"country\": \"United States\"\n        }\n      },\n      \"ein\": \"977-175\",\n      \"ssn\": \"900-590-289\",\n      \"userAgent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36\",\n      \"crypto\": {\n        \"coin\": \"Bitcoin\",\n        \"wallet\": \"0xb9fc2fe63b2a6c003f1c324c3bfa53259162181a\",\n        \"network\": \"Ethereum (ERC20)\"\n      },\n      \"role\": \"admin\" // or \"moderator\", or \"user\"\n    },\n    {...},\n    {...}\n    // 30 items\n  ],\n  \"total\": 208,\n  \"skip\": 0,\n  \"limit\": 30\n}";

    @Value("${root.username}")
    private String username;

    @Value("${root.password}")
    private String password;

    @Autowired
    private APPIdMapper appIdMapper;

    @Autowired
    private EnvironmentMapper environmentMapper;

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository, CountryRepository countryRepository,
                                               RoleRepository roleRepository, EnvironmentRepository environmentRepository,
                                               EnvironmentService environmentService, EnvironmentMapper environmentMapper,
                                               DefaultUUIDGeneratorService defaultUUIDGeneratorService,
                                               PasswordEncoder passwordEncoder,
                                               APPIdService appIdService, APPIdMapper appIdMapper,
                                               OrganizationService organizationService, OrganizationMapper organizationMapper,
                                               TeamService teamService, TeamMapper teamMapper) {
        return args -> {


            if (false) {
                Country ie = new Country(defaultUUIDGeneratorService.generateId(), "Ireland", "IE");
                countryRepository.deleteAll();
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Ethiopia", "ET"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "United Kingdom", "UK"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Germany", "DE"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Romanian", "RO"));
                countryRepository.save(ie);
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "South Africa", "ZA"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Turkey", "TR"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Albania", "AL"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Kenya", "KE"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Tanzania", "TZ"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Czech Republic", "CZ"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Greece", "GR"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Egypt", "EG"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Portugal", "PT"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Spain", "ES"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Mozambique", "MZ"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Democratic Republic of the Congo", "DCR"));
                countryRepository.save(new Country(defaultUUIDGeneratorService.generateId(), "Lesoto", "LS"));

                roleRepository.deleteAll();
                Role role = new Role();
                role.setId(defaultUUIDGeneratorService.generateId());
                role.setName(RoleConstants.SUPER_ADMIN_ROLE);
                role.setPermissions(Set.of(ADMINISTRATOR));
                roleRepository.save(role);

                Role role2 = new Role();
                role2.setId(defaultUUIDGeneratorService.generateId());
                role2.setName(RoleConstants.USER_ROLE);
                role2.setPermissions(Set.of(ADMINISTRATOR));
                roleRepository.save(role2);

                Role role3 = new Role();
                role3.setId(defaultUUIDGeneratorService.generateId());
                role3.setName(RoleConstants.USER_ROLE);
                role3.setPermissions(Set.of(CREATE_CONFIGURATION, VIEW_CONFIGURATION, UPDATE_CONFIGURATION));
                roleRepository.save(role3);

                organizationService.deleteAll();
                OrganizationDto organizationDto = createOrganizationDto("VF-1");
                Organization organizationDb1 =
                        organizationService.createOrganization(organizationMapper.convertDtoToOrganization(organizationDto));

                OrganizationDto organizationDto2 = createOrganizationDto("VF-2");
                Organization organizationDb2 =
                        organizationService.createOrganization(organizationMapper.convertDtoToOrganization(organizationDto2));


                RequestInfo requestInfo = new RequestInfo(UUID.randomUUID().toString().replace("-", ""), null, organizationDb1.getId());
                RequestContext.setRequestContext(requestInfo);

                teamService.deleteAll();
                organizationDto = organizationMapper.convertOrganizationToDto(organizationDb1);
                TeamDto teamDto = new TeamDto();
                teamDto.setName("T-UI");
                teamDto.setOrganization(organizationDto);
                teamDto = teamMapper.convertTeamToDto(teamService.createTeam(teamMapper.convertDtoToTeam(teamDto), organizationDto.getId()));
                RequestContext.clearRequestContext();


                requestInfo = new RequestInfo(UUID.randomUUID().toString().replace("-", ""), null, organizationDb1.getId());
                RequestContext.setRequestContext(requestInfo);

                organizationDto2 = organizationMapper.convertOrganizationToDto(organizationDb2);
                TeamDto teamDto2 = new TeamDto();
                teamDto2.setName("S-UI");
                teamDto2.setOrganization(organizationDto2);
                teamDto2 = teamMapper.convertTeamToDto(teamService.createTeam(teamMapper.convertDtoToTeam(teamDto2), organizationDto2.getId()));
                RequestContext.clearRequestContext();


                userRepository.deleteAll();
                User root = new User();
                root.setId(defaultUUIDGeneratorService.generateId());
                root.setCountryAccess(List.of(ie));
                root.setUsername(username);
                root.setPassword(passwordEncoder.encode(password));
                root.setOrganization(organizationDb1);
                root.setTeams(List.of(teamMapper.convertDtoToTeam(teamDto)));
                root.setRole(role);

                userRepository.save(root);

                root = new User();
                root.setId(defaultUUIDGeneratorService.generateId());
                root.setCountryAccess(List.of(ie));
                root.setUsername("manish2");
                root.setPassword(passwordEncoder.encode(password));
                root.setOrganization(organizationDb2);
                root.setTeams(List.of(teamMapper.convertDtoToTeam(teamDto2)));
                root.setRole(role2);
                userRepository.save(root);

                root = new User();
                root.setId(defaultUUIDGeneratorService.generateId());
                root.setCountryAccess(List.of(ie));
                root.setUsername("manish3");
                root.setPassword(passwordEncoder.encode(password));
                root.setOrganization(organizationDb1);
                root.setTeams(List.of(teamMapper.convertDtoToTeam(teamDto)));
                root.setRole(role3);
                userRepository.save(root);

                requestInfo = new RequestInfo(UUID.randomUUID().toString().replace("-", ""), teamDto.getId(), organizationDto.getId());
                RequestContext.setRequestContext(requestInfo);

                environmentRepository.deleteAll();
                appIdService.deleteAll();

                AppIdDto groupDevAppDto1 = new AppIdDto(ApplicationConstants.DEV_KEY_1, ApplicationConstants.DEV_KEY_1, "Group Dev App Id", organizationDb1.getId(), teamDto.getId());

                AppIdDto groupDevAppDto2 = new AppIdDto(DEV_KEY_2, DEV_KEY_2, "Group Dev App Id 2", organizationDb1.getId(), teamDto.getId());

                AppIdDto groupQaAppDto1 = new AppIdDto(QA_KEY_1, QA_KEY_1, "Group QA App Id", organizationDb1.getId(), teamDto.getId());
                AppIdDto groupQaAppDto2 = new AppIdDto(QA_KEY_2, QA_KEY_2, "Group QA App Id 2", organizationDb1.getId(), teamDto.getId());

                AppIdDto groupDevAppId1 = appIdMapper.convertAPPIdToDto(appIdService.createAPPId(appIdMapper.convertAPPIdDtoToAppId(groupDevAppDto1)));
                AppIdDto groupDevAppId2 = appIdMapper.convertAPPIdToDto(appIdService.createAPPId(appIdMapper.convertAPPIdDtoToAppId(groupDevAppDto2)));

                AppIdDto groupQaAppId1 = appIdMapper.convertAPPIdToDto(appIdService.createAPPId(appIdMapper.convertAPPIdDtoToAppId(groupQaAppDto1)));

                AppIdDto groupQaAppId2 = appIdMapper.convertAPPIdToDto(appIdService.createAPPId(appIdMapper.convertAPPIdDtoToAppId(groupQaAppDto2)));

                List<AppIdDto> appIdsGroupDev = List.of(groupDevAppId1, groupDevAppId2);
                List<AppIdDto> appIdsGroupQa = List.of(groupQaAppId1, groupQaAppId2);


                CreateEnvironmentDto environmentDto = createEnvDto(organizationDto, teamDto, "DEV", DEV_URL,
                        DEV_URL, appIdsGroupDev, defaultConfig, "DEV");

                environmentMapper.convertEnvironmentToCreateDto(
                        environmentService.createEnvironment(environmentMapper.convertCreateDtoToEnvironment(environmentDto)));

                CreateEnvironmentDto environmentDto2 = createEnvDto(organizationDto, teamDto, "QA", QA_URL,
                        QA_URL, appIdsGroupQa, defaultConfig, "QA");

                environmentMapper.convertEnvironmentToCreateDto(
                        environmentService.createEnvironment(environmentMapper.convertCreateDtoToEnvironment(environmentDto2)));
                System.out.println(teamDto.getId());
                System.out.println(organizationDto.getId());
                RequestContext.clearRequestContext();
            }
        };
    }


    private static OrganizationDto createOrganizationDto(String orgName) {
        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setName(orgName);
        return organizationDto;
    }

    private static CreateEnvironmentDto createEnvDto(OrganizationDto organizationDto, TeamDto teamDto, String environmentName, String gatewayURL, String tokenURL, List<AppIdDto> appIds, String defaultConfig, String envType) {

        CreateEnvironmentDto environment = createEnvironment(environmentName, gatewayURL, tokenURL);
        environment.setId(UUID.randomUUID().toString());
        environment.setDefaultConfigTemplate(defaultConfig);
        environment.setAppIds(appIds.stream().map(AppIdDto::getId).toList());
        environment.setOrganizationId(organizationDto.getId());
        environment.setTeamId(teamDto.getId());
        environment.setEnvironmentType(envType);
        return environment;
    }

    private static CreateEnvironmentDto createEnvironment(String name, String hostUrl, String authTokenUrl) {
        CreateEnvironmentDto environment = new CreateEnvironmentDto();
        environment.setId(UUID.randomUUID().toString());
        environment.setName(name);
        environment.setHostUrl(hostUrl);
        environment.setAuthTokenUrl(authTokenUrl);
        return environment;
    }
}
