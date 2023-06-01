package com.appsdeveloperblog.estore.productservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Builder
@Data
public class CreateProductCommand {
    private final String title;
    private final BigDecimal price;
    private final Integer quantity;
    @TargetAggregateIdentifier
    private final String productId;


}
