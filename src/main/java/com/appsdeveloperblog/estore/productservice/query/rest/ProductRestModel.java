package com.appsdeveloperblog.estore.productservice.query.rest;

import lombok.Data;

@Data
public class ProductRestModel {
    private String productId;
    private String title;
    private String price;
    private Integer quantity;
}
