package com.zest.productapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.productapi.dto.ProductRequestDTO;
import com.zest.productapi.dto.ProductResponseDTO;
import com.zest.productapi.exception.ResourceNotFoundException;
import com.zest.productapi.security.TestSecurityConfig;
import com.zest.productapi.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private com.zest.productapi.security.JwtTokenProvider jwtTokenProvider;

    @MockBean
    private com.zest.productapi.security.CustomUserDetailsService customUserDetailsService;

    @MockBean
    private com.zest.productapi.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    // -----------------------------
    // READ Tests
    // -----------------------------
    @Test
    void shouldReturnProductById() throws Exception {
        ProductResponseDTO dto = ProductResponseDTO.builder()
                .id(1)
                .productName("Mobile")
                .createdBy("Admin")
                .createdOn(LocalDateTime.now())
                .build();

        when(productService.getProductById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Mobile"));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        when(productService.getProductById(1))
                .thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    // -----------------------------
    // CREATE Tests
    // -----------------------------
    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setProductName("Laptop");
        request.setCreatedBy("Admin");

        ProductResponseDTO response = ProductResponseDTO.builder()
                .id(2)
                .productName("Laptop")
                .createdBy("Admin")
                .createdOn(LocalDateTime.now())
                .build();

        when(productService.createProduct(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Laptop"));
    }

    @Test
    void shouldReturnBadRequestWhenProductNameIsBlank() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setProductName(""); // blank
        request.setCreatedBy("Admin");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCreatedByIsBlank() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setProductName("Laptop");
        request.setCreatedBy(""); // blank

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenAllFieldsAreBlank() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO(); // all blank

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // -----------------------------
    // DELETE Tests
    // -----------------------------
    @Test
    void shouldDeleteProduct() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(1);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }
}