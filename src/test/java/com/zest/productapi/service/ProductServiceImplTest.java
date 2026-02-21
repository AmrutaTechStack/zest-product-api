package com.zest.productapi.service;

import com.zest.productapi.dto.ProductRequestDTO;
import com.zest.productapi.dto.ProductResponseDTO;
import com.zest.productapi.entity.Product;
import com.zest.productapi.exception.ResourceNotFoundException;
import com.zest.productapi.repo.ProductRepository;
import com.zest.productapi.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldReturnProductById() {
        Product product = new Product();
        product.setId(1);
        product.setProductName("Laptop");
        product.setCreatedBy("Admin");
        product.setCreatedOn(LocalDateTime.now());

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductResponseDTO response = productService.getProductById(1);

        assertEquals("Laptop", response.getProductName());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1));
    }

    @Test
    void shouldCreateProduct() {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setProductName("Tablet");
        request.setCreatedBy("Admin");

        Product savedProduct = new Product();
        savedProduct.setId(1);
        savedProduct.setProductName("Tablet");
        savedProduct.setCreatedBy("Admin");
        savedProduct.setCreatedOn(LocalDateTime.now());

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDTO response = productService.createProduct(request);

        assertEquals("Tablet", response.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldDeleteProduct() {
        // Arrange
        doNothing().when(productRepository).deleteById(1);

        // Act
        productService.deleteProduct(1);

        // Assert
        verify(productRepository, times(1)).deleteById(1); // match the actual call
    }
}
