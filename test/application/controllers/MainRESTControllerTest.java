package application.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.persistence.OptimisticLockException;
import javax.servlet.ServletContext;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import application.configs.root.CommonConfig;
import application.configs.root.ProdConfig;
import application.configs.root.TestConfig;
import application.configs.web.WebConfig;
import application.dao.Dao;
import application.entities.Genre;
import application.services.exceptions.ConstraintException;
import application.services.exceptions.NoSuchRecord;
import application.services.exceptions.TableNameRequestBodyException;
import application.services.exceptions.WrongTableNameException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebConfig.class, CommonConfig.class, ProdConfig.class, TestConfig.class })
@ActiveProfiles("test")
@WebAppConfiguration
@TestMethodOrder(OrderAnnotation.class)
public class MainRESTControllerTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Autowired
	private Dao dao;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	@Order(1)
	@DisplayName("servlet context contains proper controllers")
	public void test1() {
		ServletContext servletContext = wac.getServletContext();
		assertNotNull(servletContext);
		assertTrue(servletContext instanceof MockServletContext);
		assertNotNull(wac.getBean(MainRESTController.class));
		assertNotNull(wac.getBean(MainController.class));
	}

	@Test
	@Order(2)
	@DisplayName("findAll - response is empty, when DB is empty")
	public void test2() throws Exception {
		mockMvc.perform(get("/mainPage/Genre"))//
//					.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//
				.andExpect(content().string("[]")).andReturn();
	}

	@Test
	@Order(3)
	@DisplayName("findAll - controller returns proper response, when added record to DB")
	public void test3() throws Exception {
		Genre genre1 = new Genre();
		genre1.setName("gatunek1");
		dao.save(genre1); // 1+ save

		Genre genre2 = new Genre();
		genre2.setName("gatunek2");
		dao.save(genre2); // 1+ save

		mockMvc.perform(get("/mainPage/Genre"))//
//					.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//
				.andExpect(jsonPath("$[0].id").value(1))//
				.andExpect(jsonPath("$[0].name").value("gatunek1"))//
				.andExpect(jsonPath("$[1].id").value(2))//
				.andExpect(jsonPath("$[1].name").value("gatunek2"))//
				.andReturn();
	}

	@Test
	@Order(4)
	@DisplayName("findAll - proper response is returned when request tableName is wrong")
	public void test4() throws Exception {
		mockMvc.perform(get("/mainPage/casdasd"))
//				.andDo(print())//
				.andExpect(status().isBadRequest())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(
						jsonPath("$.errorMessage").value(MessageFormat.format(WrongTableNameException.ERROR_MESSAGE, //
								ClassNotFoundException.class.getSimpleName(), "casdasd")))//
				.andExpect(jsonPath("$.solutions").value(Arrays.stream(WrongTableNameException.SOLUTIONS)//
						.collect(Collectors.joining(", "))))
				.andReturn();
	}

	@Test
	@Order(5)
	@DisplayName("findById - works fine")
	public void test5() throws Exception {
		mockMvc.perform(get("/mainPage/Genre/1"))
//				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//
				.andExpect(jsonPath("$.id").value(1))//
				.andExpect(jsonPath("$.name").value("gatunek1"))//
				.andReturn();
	}

	@Test
	@Order(6)
	@DisplayName("findById - returns 404 when record with fiven id doesn't exist in DB")
	public void test6() throws Exception {
		mockMvc.perform(get("/mainPage/Genre/13"))
//				.andDo(print())//
				.andExpect(status().isNotFound())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(
						jsonPath("$.errorMessage").value(MessageFormat.format(NoSuchRecord.ERROR_MESSAGE, //
								NoSuchElementException.class.getSimpleName(), "Genre",13)))//
				.andExpect(jsonPath("$.solutions").value(Arrays.stream(NoSuchRecord.SOLUTIONS)//
						.collect(Collectors.joining(", "))))
				.andReturn();
	}

	@Test
	@Order(7)
	@DisplayName("findById - proper response is returned when request tableName is wrong")
	public void test7() throws Exception {
		mockMvc.perform(get("/mainPage/casdasd/1"))//
//				.andDo(print())//
				.andExpect(status().isBadRequest())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(
						jsonPath("$.errorMessage").value(MessageFormat.format(WrongTableNameException.ERROR_MESSAGE, //
								ClassNotFoundException.class.getSimpleName(), "casdasd")))//
				.andExpect(jsonPath("$.solutions").value(Arrays.stream(WrongTableNameException.SOLUTIONS)//
						.collect(Collectors.joining(", "))))
				.andReturn();
	}

	@Test
	@Order(8)
	@DisplayName("update - works fine")
	public void test8() throws Exception {
		Genre genre = new Genre();
		genre.setId(1);
		genre.setName("nowa fajna nazwa");

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(genre);

		mockMvc.perform(put("/mainPage/Genre/" + genre.getId()).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(requestJson))//
//				.andDo(print())//
				.andExpect(status().isOk())//
				.andReturn();

		mockMvc.perform(get("/mainPage/Genre/" + genre.getId()))//
//				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//
				.andExpect(jsonPath("$.id").value(1))//
				.andExpect(jsonPath("$.name").value("nowa fajna nazwa"))//
				.andReturn();
	}

	@Test
	@Order(9)
	@DisplayName("update - returns proper response, when passed tableName is wrong")
	public void test9() throws Exception {
		Genre genre = new Genre();
		genre.setId(1);
		genre.setName("nowa fajna nazwa");

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(genre);

		mockMvc.perform(put("/mainPage/asdads/" + genre.getId()).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(requestJson))//
//				.andDo(print())//
				.andExpect(status().isBadRequest())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(
						jsonPath("$.errorMessage").value(MessageFormat.format(WrongTableNameException.ERROR_MESSAGE, //
								ClassNotFoundException.class.getSimpleName(), "asdads")))//
				.andExpect(jsonPath("$.solutions").value(Arrays.stream(WrongTableNameException.SOLUTIONS)//
						.collect(Collectors.joining(", "))))
				.andReturn();
	}

	@Test
	@Order(10)
	@DisplayName("update - when request body doesn't match tableName exception is thrown")
	public void test10() throws Exception {
		Genre genre = new Genre();
		genre.setId(1);
		genre.setName("nowa fajna nazwa");

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(genre);

		String expectedResponse = requestJson.replaceAll("\"", "").replaceAll(":", "=").replaceAll(",", ", ");

		mockMvc.perform(put("/mainPage/AgeCategory/" + genre.getId()).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(requestJson))//
//				.andDo(print())//
				.andExpect(status().isBadRequest())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.errorMessage")
						.value(MessageFormat.format(TableNameRequestBodyException.ERROR_MESSAGE, //
								OptimisticLockException.class.getSimpleName(), "AgeCategory", expectedResponse)))//
				.andExpect(jsonPath("$.solutions").value(Arrays.stream(TableNameRequestBodyException.SOLUTIONS)//
						.collect(Collectors.joining(", "))))
				.andReturn();
	}

	@Test
	@Order(11)
	@DisplayName("update - when unique constraint is broken, exception is thrown")
	public void test11() throws Exception {
		Genre genre = new Genre();
		genre.setId(2);
		genre.setName("nowa fajna nazwa");

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(genre);

		mockMvc.perform(put("/mainPage/Genre/" + genre.getId()).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(requestJson))//
//				.andDo(print())//
				.andExpect(status().isForbidden())//
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//
				.andExpect(jsonPath("$.errorMessage")
						.value(MessageFormat.format(ConstraintException.ERROR_MESSAGE, //
								ConstraintViolationException.class.getSimpleName(), "Genre")))//
				.andExpect(jsonPath("$.solutions").value(Arrays.stream(ConstraintException.SOLUTIONS)
						.collect(Collectors.joining(", "))))//
				.andReturn();
	}

}
