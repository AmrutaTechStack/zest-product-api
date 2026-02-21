package com.zest.productapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "item",
        indexes = {
                @Index(name = "idx_item_product_id", columnList = "product_id")
        }
)
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
    @SequenceGenerator(
            name = "item_seq_gen",
            sequenceName = "item_seq",
            allocationSize = 1
    )
    private Integer id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
