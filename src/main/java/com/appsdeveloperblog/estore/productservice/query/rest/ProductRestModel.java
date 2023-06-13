package com.appsdeveloperblog.estore.productservice.query.rest;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class ProductRestModel {
    private String productId;
    @NotBlank(message="product title is a required field")
    private String title;
    @Min(value=1, message="price cannot be lower than 1")
    private String price;
    @Min(value=1, message="quantity cannot be lower than 1")
    @Max(value=5, message="quantity cannot be lower than 5")
    private Integer quantity;
}
