package com.appsdeveloperblog.estore.productservice.command;

import com.appsdeveloperblog.estore.core.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.core.events.ProductReservationCancelledEvent;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.productservice.core.events.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {
    Logger logger = LoggerFactory.getLogger(ProductAggregate.class);
    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;


    public ProductAggregate(){

    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) throws Exception{
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
       // if(true) throw new Exception("An error took place in the create product command @commanHalder method");
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent){
        this.productId = productCreatedEvent.getProductId();
        this.price = productCreatedEvent.getPrice();
        this.title = productCreatedEvent.getTitle();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand){
        //write business logic to check if the product quantity is greater than the incoming to be reserved product quantity
        if(quantity<reserveProductCommand.getQuantity()){
            throw new IllegalArgumentException("Insufficient no. of items in the stock");
        }
        //create a new instance of ProductReservedEvent
        ProductReservedEvent productReservedEvent = new ProductReservedEvent()
                .builder()
                .orderId(reserveProductCommand.getOrderId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();
        // call apply method to apply this event to aggregate
        AggregateLifecycle.apply(productReservedEvent);

    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent){
        logger.info("productReservedEvent in eventsourcinghandler called====");
        this.quantity -= productReservedEvent.getQuantity();
    }

    @CommandHandler
    public void on(CancelProductReservationCommand cancelProductReservationCommand){
        ProductReservationCancelledEvent productReservationCancelledEvent = ProductReservationCancelledEvent
                .builder()
                .orderId(cancelProductReservationCommand.getOrderId())
                .productId(cancelProductReservationCommand.getProductId())
                .quantity(cancelProductReservationCommand.getQuantity())
                .userId(cancelProductReservationCommand.getUserId())
                .reason(cancelProductReservationCommand.getReason())
                .build();
        AggregateLifecycle.apply(productReservationCancelledEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent){
        this.quantity += productReservationCancelledEvent.getQuantity();
    }
}
