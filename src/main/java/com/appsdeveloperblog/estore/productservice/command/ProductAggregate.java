package com.appsdeveloperblog.estore.productservice.command;

import com.appsdeveloperblog.estore.productservice.core.events.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

    public ProductAggregate(){

    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand){
        //validation logic
        if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("price cannot be less or equa than zero");
        }
        if(createProductCommand.getTitle()==null || createProductCommand.getTitle().isBlank()){
            throw new IllegalArgumentException("Title cannot be empty");
        }

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
        AggregateLifecycle.apply(productCreatedEvent);
    }
}
