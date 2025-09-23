package com.example.buisines_service.communication;

import com.example.buisines_service.dto.CategoriaDTO;
import com.example.buisines_service.dto.ProductoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommunicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static WireMockServer wireMockServer;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        registry.add("data.service.url", wireMockServer::baseUrl);
    }

    @AfterAll
    static void teardown() {
        wireMockServer.stop();
    }
    
    @Test
    void GET_ProductoPorID_exito() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(
                        urlEqualTo("/data/productos/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"nombre\":\"Laptop\",\"precio\":1000.50}")));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop"));

        wireMockServer.verify(getRequestedFor(urlEqualTo("/data/productos/1")));
    }

    @Test
    void GET_ProductoPorID_notFound() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(
                        com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo("/data/productos/999"))
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withStatus(404)));

        mockMvc.perform(get("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void GET_ProductoporId_serviceUnavailable() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(
                        com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo("/data/productos/1"))
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withStatus(500)));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isServiceUnavailable());

    }

    @Test
    void POST_Producto_exito() throws Exception {
        ProductoRequest request = new ProductoRequest("SmartPhone", "Blablabla", BigDecimal.valueOf(15), 5, 1L);

        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(
                        urlEqualTo("/data/productos"))
                .withRequestBody(com.github.tomakehurst.wiremock.client.WireMock.equalToJson(
                        objectMapper.writeValueAsString(request)))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":2,\"nombre\":\"SmartPhone\"}")));

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("SmartPhone"));
    }


    @Test
    void POST_Producto_validationError() throws Exception {
        ProductoRequest request = new ProductoRequest("RTX4070", "BlaBlaBla", BigDecimal.valueOf(-10), 5, 1L);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void PUT_Producto_exito() throws Exception {
        ProductoRequest request = new ProductoRequest("RTX4070", "BlaBlaBla", BigDecimal.valueOf(20), 10, 1L);

        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.put(
                        urlEqualTo("/data/productos/1"))
                .withRequestBody(com.github.tomakehurst.wiremock.client.WireMock.equalToJson(
                        objectMapper.writeValueAsString(request)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"nombre\":\"RTX4070\"}")));

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("RTX4070"));
    }

    @Test
    void DELETE_Producto_exito() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.delete(
                        urlEqualTo("/data/productos/1"))
                .willReturn(aResponse().withStatus(204)));

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void GET_TodasLasCategorias_exito() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(
                        urlEqualTo("/data/categorias"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":1,\"nombre\":\"Computadoras\"}]")));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Computadoras"));
    }

    @Test
    void PUT_Categoria_exito() throws Exception {
        CategoriaDTO categoria = new CategoriaDTO(null, "CPUs", "Desc C");

        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.put(
                        urlEqualTo("/data/categorias/1"))
                .withRequestBody(com.github.tomakehurst.wiremock.client.WireMock.equalToJson(
                        objectMapper.writeValueAsString(categoria)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"nombre\":\"CPUs\"}")));

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("CPUs"));
    }

    @Test
    void DELETE_Categoria_exito() throws Exception {
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.delete(
                        urlEqualTo("/data/categorias/1"))
                .willReturn(aResponse().withStatus(204)));

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }
}
