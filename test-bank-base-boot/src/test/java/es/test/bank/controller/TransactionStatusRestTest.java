package es.test.bank.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.c4_soft.springaddons.security.oauth2.test.annotations.Claims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.StringClaim;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.KeycloakAccess;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.KeycloakAccessToken;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.KeycloakAuthorization;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.KeycloakPermission;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.test.bank.dto.ChannelDTO;
import es.test.bank.dto.StatusDTO;
import es.test.bank.dto.TransactionStatusRequestDTO;
import es.test.bank.dto.TransactionStatusResponseDTO;
import es.test.bank.model.AccountModel;
import es.test.bank.model.TransactionModel;
import es.test.bank.service.AccountService;
import es.test.bank.service.TransactionService;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@WebAppConfiguration
@Slf4j
@ComponentScan(basePackageClasses = { KeycloakSecurityComponents.class, KeycloakSpringBootConfigResolver.class })
@TypeExcludeFilters(KafkaListenersTypeExcludeFilter.class)
class TransactionStatusRestTest {

	private static final String BASE_ENDPOINT = "/testbank";
	private static final String TRANSACTIONS_ENDPOINT = BASE_ENDPOINT + "/transactions";
	private static final String ACCOUNT_ENDPOINT = TRANSACTIONS_ENDPOINT + "/status";

	private final WebApplicationContext applicationContext;
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	public TransactionStatusRestTest(WebApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@BeforeAll
	static void inserts(@Autowired AccountService accountService, @Autowired TransactionService transactionService) throws Exception {
		AccountModel accountModel = new AccountModel("3", 1800.90);
		accountService.createAccount(accountModel);
		OffsetDateTime date = OffsetDateTime.parse("2022-10-02T20:15:30.000Z");
		TransactionModel transactionModel = new TransactionModel("1223", "3", date, 120.01, 12.4,"description");
		transactionService.createTransaction(transactionModel);
		date = OffsetDateTime.now();
		transactionModel = new TransactionModel("55", "3", date, 50.10, 3.4,"description2");
		transactionService.createTransaction(transactionModel);
		accountModel = new AccountModel("4", 1600.90);
		accountService.createAccount(accountModel);
		date = OffsetDateTime.parse("2022-05-02T20:15:30.000Z");
		transactionModel = new TransactionModel("122", "4", date, 100.10, 3.4,"description2");
		transactionService.createTransaction(transactionModel);
    }

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
	}

	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetStatusTest_NotExitReference_ResponseOKWithStatusInvalid() throws Exception {
		LOGGER.info("GetStatusTest_NotExitReference_ResponseOKWithStatusInvalid");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		TransactionStatusRequestDTO example = new TransactionStatusRequestDTO("1124", ChannelDTO.ATM);
		String exampleJson = objectMapper.writeValueAsString(example);
		MvcResult mvcResult = this.mockMvc
				.perform(post(ACCOUNT_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
		TransactionStatusResponseDTO exampleResult = objectMapper.readValue(content, TransactionStatusResponseDTO.class);
		assertEquals(exampleResult.getStatus(), StatusDTO.INVALID);
	}
	
	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetStatusTest_ExitReferenceWithClientChannel_ResponseOKWithDatePast() throws Exception {
		LOGGER.info("GetStatusTest_ExitReferenceWithClientChannel_ResponseOKWithDatePast");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		TransactionStatusRequestDTO example = new TransactionStatusRequestDTO("122", ChannelDTO.CLIENT);
		String exampleJson = objectMapper.writeValueAsString(example);
		MvcResult mvcResult = this.mockMvc
				.perform(post(ACCOUNT_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
		TransactionStatusResponseDTO exampleResult = objectMapper.readValue(content, TransactionStatusResponseDTO.class);
		assertEquals(exampleResult.getStatus(), StatusDTO.SETTLED);
	}
	
	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetStatusTest_ExitReferenceWithClientChannel_ResponseOKWithDatePresent() throws Exception {
		LOGGER.info("GetStatusTest_ExitReferenceWithClientChannel_ResponseOKWithDatePresent");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		TransactionStatusRequestDTO example = new TransactionStatusRequestDTO("55", ChannelDTO.CLIENT);
		String exampleJson = objectMapper.writeValueAsString(example);
		MvcResult mvcResult = this.mockMvc
				.perform(post(ACCOUNT_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
		TransactionStatusResponseDTO exampleResult = objectMapper.readValue(content, TransactionStatusResponseDTO.class);
		assertEquals(exampleResult.getStatus(), StatusDTO.PENDING);
	}

	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetStatusTest_ExitReferenceWithClientChannel_ResponseOKWithDateFuture() throws Exception {
		LOGGER.info("GetStatusTest_ExitReferenceWithClientChannel_ResponseOKWithDateFuture");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		TransactionStatusRequestDTO example = new TransactionStatusRequestDTO("1223", ChannelDTO.CLIENT);
		String exampleJson = objectMapper.writeValueAsString(example);
		MvcResult mvcResult = this.mockMvc
				.perform(post(ACCOUNT_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
		TransactionStatusResponseDTO exampleResult = objectMapper.readValue(content, TransactionStatusResponseDTO.class);
		assertEquals(exampleResult.getStatus(), StatusDTO.FUTURE);
	}

	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetStatusTest_ExitReferenceWithAtmChannel_ResponseOKWithDateFuture() throws Exception {
		LOGGER.info("GetStatusTest_ExitReferenceWithClientChannel_ResponseOKWithDateFuture");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		TransactionStatusRequestDTO example = new TransactionStatusRequestDTO("1223", ChannelDTO.ATM);
		String exampleJson = objectMapper.writeValueAsString(example);
		MvcResult mvcResult = this.mockMvc
				.perform(post(ACCOUNT_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
		TransactionStatusResponseDTO exampleResult = objectMapper.readValue(content, TransactionStatusResponseDTO.class);
		assertEquals(exampleResult.getStatus(), StatusDTO.PENDING);
	}
	
	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetStatusTest_ExitReferenceWithInternalChannel_ResponseOKWithDatePast() throws Exception {
		LOGGER.info("GetStatusTest_ExitReferenceWithInternalChannel_ResponseOKWithDatePast");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		TransactionStatusRequestDTO example = new TransactionStatusRequestDTO("122", ChannelDTO.INTERNAL);
		String exampleJson = objectMapper.writeValueAsString(example);
		MvcResult mvcResult = this.mockMvc
				.perform(post(ACCOUNT_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
		TransactionStatusResponseDTO exampleResult = objectMapper.readValue(content, TransactionStatusResponseDTO.class);
		assertEquals(exampleResult.getStatus(), StatusDTO.SETTLED);
	}
	
	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetStatusTest_ExitReferenceWithInternalChannel_ResponseOKWithDatePresent() throws Exception {
		LOGGER.info("GetStatusTest_ExitReferenceWithInternalChannel_ResponseOKWithDatePresent");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		TransactionStatusRequestDTO example = new TransactionStatusRequestDTO("55", ChannelDTO.INTERNAL);
		String exampleJson = objectMapper.writeValueAsString(example);
		MvcResult mvcResult = this.mockMvc
				.perform(post(ACCOUNT_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
		TransactionStatusResponseDTO exampleResult = objectMapper.readValue(content, TransactionStatusResponseDTO.class);
		assertEquals(exampleResult.getStatus(), StatusDTO.PENDING);
	}

	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetStatusTest_ExitReferenceWithInternalChannel_ResponseOKWithDateFuture() throws Exception {
		LOGGER.info("GetStatusTest_ExitReferenceWithInternalChannel_ResponseOKWithDateFuture");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		TransactionStatusRequestDTO example = new TransactionStatusRequestDTO("1223", ChannelDTO.INTERNAL);
		String exampleJson = objectMapper.writeValueAsString(example);
		MvcResult mvcResult = this.mockMvc
				.perform(post(ACCOUNT_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
		TransactionStatusResponseDTO exampleResult = objectMapper.readValue(content, TransactionStatusResponseDTO.class);
		assertEquals(exampleResult.getStatus(), StatusDTO.FUTURE);
	}
	
}