package es.test.bank.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.List;

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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.test.bank.dto.CreateTransactionRequestDTO;
import es.test.bank.dto.TransactionDTO;
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
class TransactionsRestTest {

	private static final String BASE_ENDPOINT = "/testbank";
	private static final String TRANSACTIONS_ENDPOINT = BASE_ENDPOINT + "/transactions";

	private final WebApplicationContext applicationContext;
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	public TransactionsRestTest(WebApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@BeforeAll
	static void inserts(@Autowired AccountService accountService, @Autowired TransactionService transactionService) throws Exception {
		AccountModel accountModel = new AccountModel("1", 1800.90);
		accountService.createAccount(accountModel);
		OffsetDateTime date = OffsetDateTime.parse("2022-10-02T20:15:30.000Z");
		TransactionModel transactionModel = new TransactionModel("1213", "1", date, 120.01, 12.4,"description");
		transactionService.createTransaction(transactionModel);
		date = OffsetDateTime.now();
		transactionModel = new TransactionModel("5", "1", date, 50.10, 3.4,"description2");
		transactionService.createTransaction(transactionModel);
		accountModel = new AccountModel("2", 1600.90);
		accountService.createAccount(accountModel);
		date = OffsetDateTime.parse("2022-05-02T20:15:30.000Z");
		transactionModel = new TransactionModel("112", "2", date, 100.10, 3.4,"description2");
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
	void CreateTest_ExistAccountIban_ResponseOK() throws Exception {
		LOGGER.info("CreateTest_ExistAccountIban_ResponseOK");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		OffsetDateTime date = OffsetDateTime.parse("2016-10-02T20:15:30.000Z");
		CreateTransactionRequestDTO example = new CreateTransactionRequestDTO("13123", "1", date, 125.01, 12.4,
				"description");
		String exampleJson = objectMapper.writeValueAsString(example);
		this.mockMvc
				.perform(post(TRANSACTIONS_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isCreated()).andReturn();
	}

	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void CreateTest_ExistAccountIbanButAmountMayorAmountAccount_ResponseKO() throws Exception {
		LOGGER.info("CreateTest_ExistAccountIbanButAmountMayorAmountAccount_ResponseKO");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		OffsetDateTime date = OffsetDateTime.parse("2016-10-02T20:15:30.000Z");
		CreateTransactionRequestDTO example = new CreateTransactionRequestDTO("44", "1", date, 120000.00, 12.4,
				"description");
		String exampleJson = objectMapper.writeValueAsString(example);
		this.mockMvc
				.perform(post(TRANSACTIONS_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isNotAcceptable()).andReturn();
	}

	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void CreateTest_NotFoundAccount_ResponseKO() throws Exception {
		LOGGER.info("CreateTest_NotFoundAccount_ResponseKO");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		OffsetDateTime date = OffsetDateTime.parse("2016-10-02T20:15:30.000Z");
		CreateTransactionRequestDTO example = new CreateTransactionRequestDTO("2", "3", date, 125.01, 12.4,
				"description");
		String exampleJson = objectMapper.writeValueAsString(example);
		this.mockMvc
				.perform(post(TRANSACTIONS_ENDPOINT).header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(exampleJson))
				.andExpect(status().isNotFound()).andReturn();
	}
	
	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetTransactionsTest_WithIbanAndAscendingOrder_ResponseOK() throws Exception {
		LOGGER.info("GetTransactionsTest_WithIbanAndAscendingOrder_ResponseOK");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		MvcResult mvcResult = this.mockMvc
				.perform(get(TRANSACTIONS_ENDPOINT).header("Authorization", "Bearer " + token)
						.param("account_iban", "1")
						.param("sort", "ascending"))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
	}
	
	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetTransactionsTest_WithIbanButWithoutOrder_ResponseOK() throws Exception {
		LOGGER.info("GetTransactionsTest_WithIbanButWithoutOrder_ResponseOK");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		MvcResult mvcResult = this.mockMvc
				.perform(get(TRANSACTIONS_ENDPOINT).header("Authorization", "Bearer " + token)
						.param("account_iban", "1"))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
	}
	
	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetTransactionsTest_WithIbanAndDescendingOrder_ResponseOK() throws Exception {
		LOGGER.info("GetTransactionsTest_WithIbanAndDescendingOrder_ResponseOK");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		MvcResult mvcResult = this.mockMvc
				.perform(get(TRANSACTIONS_ENDPOINT).header("Authorization", "Bearer " + token)
						.param("account_iban", "1")
						.param("sort", "descending"))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
	}
	
	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetTransactionsTest_WithoutIbanAndOrder_ResponseOK() throws Exception {
		LOGGER.info("GetTransactionsTest_WithoutIbanAndOrder_ResponseOK");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		MvcResult mvcResult = this.mockMvc
				.perform(get(TRANSACTIONS_ENDPOINT).header("Authorization", "Bearer " + token))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
	}
	
	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetTransactionsTest_WithoutIbanButDescendingOrder_ResponseOK() throws Exception {
		LOGGER.info("GetTransactionsTest_WithoutIbanButDescendingOrder_ResponseOK");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		MvcResult mvcResult = this.mockMvc
				.perform(get(TRANSACTIONS_ENDPOINT).header("Authorization", "Bearer " + token)
						.param("sort", "descending"))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
	}
	
	@Test
	@WithMockKeycloakAuth(authorities = { "USER",
			"AUTHORIZED_PERSONNEL" }, accessToken = @KeycloakAccessToken(realmAccess = @KeycloakAccess(roles = {
					"TESTER" }), authorization = @KeycloakAuthorization(permissions = @KeycloakPermission(rsid = "toto", rsname = "truc", scopes = "abracadabra"))), claims = @OpenIdClaims(sub = "42", email = "ch4mp@c4-soft.com", emailVerified = true, nickName = "Tonton-Pirate", preferredUsername = "ch4mpy", otherClaims = @Claims(stringClaims = @StringClaim(name = "foo", value = "bar"))))
	void GetTransactionsTest_NotFoundIbanButWithoutOrder_ResponseKO() throws Exception {
		LOGGER.info("GetTransactionsTest_NotFoundIbanButWithoutOrder_ResponseKO");
		SecurityContext context = SecurityContextHolder.getContext();
		KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
		AccessToken token = authentication.getAccount().getKeycloakSecurityContext().getToken();
		MvcResult mvcResult = this.mockMvc
				.perform(get(TRANSACTIONS_ENDPOINT).header("Authorization", "Bearer " + token)
						.param("account_iban", "3"))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		LOGGER.info(content);
		List<TransactionDTO> exampleResult = objectMapper.readValue(content, new TypeReference<List<TransactionDTO>>(){});
		assertTrue(exampleResult.isEmpty());
	}
}