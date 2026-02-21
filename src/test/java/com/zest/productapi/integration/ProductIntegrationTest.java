package com.zest.productapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.productapi.dto.ProductRequestDTO;
import com.zest.productapi.entity.Product;
import com.zest.productapi.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop",
                    locations = "classpath:application-test.yml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSaveAndFetchProduct() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setProductName("Monitor");
        request.setCreatedBy("Admin");

        // Create Product
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Monitor"));

        // Fetch Product
        Product savedProduct = productRepository.findAll().get(0);
        mockMvc.perform(get("/api/v1/products/" + savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Monitor"));

        // Verify in DB
        assertThat(productRepository.findAll()).hasSize(1);
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Product product = new Product();
        product.setProductName("Keyboard");
        product.setCreatedBy("Admin");
        productRepository.save(product);

        mockMvc.perform(delete("/api/v1/products/" + product.getId()))
                .andExpect(status().isNoContent());

        assertThat(productRepository.findAll()).isEmpty();
    }
}
