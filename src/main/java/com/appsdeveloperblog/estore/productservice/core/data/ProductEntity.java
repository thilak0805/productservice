package com.appsdeveloperblog.estore.productservice.core.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name="products")
@Data
public class ProductEntity implements Serializable {
    @Id
    @Column(unique = true)
    private String productId;
    @Column
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
