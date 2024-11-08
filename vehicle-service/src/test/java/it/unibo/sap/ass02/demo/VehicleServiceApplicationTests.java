package it.unibo.sap.ass02.demo;

import it.unibo.sap.ass02.demo.domain.EBikeImpl;
import it.unibo.sap.ass02.demo.repositories.EBikeRepository;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class VehicleServiceApplicationTests {

	@LocalServerPort
	private int port;

	private String address = "";

	@Autowired
	private TestRestTemplate restTemplate;

	@PostConstruct
	private void buildAddress() {
		this.address = "http://localhost:" + this.port + "/ebike";
	}

	@Test
	void contextLoads() {

	}

	@Test
	void postNewEBikeTest() {
		final EBikeImpl ebike = new EBikeImpl("UU3");
		assertEquals(this.restTemplate.postForEntity(this.address + "/create", ebike, EBikeImpl.class).getStatusCode(), HttpStatus.OK);
	}

	@Test
	void getAllEBikes() {
		final List<EBikeImpl> ebikes = List.of(
				new EBikeImpl(UUID.randomUUID().toString()),
				new EBikeImpl(UUID.randomUUID().toString()));
		ebikes.forEach(ebike -> {
			this.restTemplate.postForEntity(this.address + "/create", ebike, EBikeImpl.class);
		});
		final ResponseEntity<List<EBikeImpl>> response = this.restTemplate.exchange(
				this.address + "/all",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<EBikeImpl>>() {}
		);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(!Objects.requireNonNull(response.getBody()).isEmpty());
		ebikes.forEach(ebike -> {
			assertTrue(response.getBody().contains(ebike));
		});
	}

}
