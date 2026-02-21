package com.zest.productapi.service;

import com.zest.productapi.dto.ProductRequestDTO;
import com.zest.productapi.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponseDTO> getAllProducts(Pageable pageable);

    ProductResponseDTO getProductById(Integer id);

    ProductResponseDTO createProduct(ProductRequestDTO request);

    ProductResponseDTO updateProduct(Integer id, ProductRequestDTO request);

    void deleteProduct(Integer id);
}

