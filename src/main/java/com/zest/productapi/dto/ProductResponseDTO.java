package com.zest.productapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProductResponseDTO {

    private Integer id;
    private String productName;
    private String createdBy;
    private LocalDateTime createdOn;
    private String modifiedBy;        // added
    private LocalDateTime modifiedOn;
}

