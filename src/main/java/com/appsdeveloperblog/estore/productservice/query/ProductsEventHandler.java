package com.appsdeveloperblog.estore.productservice.query;

import com.appsdeveloperblog.estore.core.events.ProductReservationCancelledEvent;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.productservice.core.data.ProductEntity;
import com.appsdeveloperblog.estore.productservice.core.data.ProductRepository;
import com.appsdeveloperblog.estore.productservice.core.events.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Component
@ProcessingGroup("product-group")
public class ProductsEventHandler {
    Logger logger = LoggerFactory.getLogger(ProductsEventHandler.class);
    private final ProductRepository productRepository;

    public ProductsEventHandler(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){
        System.out.println("inside producteventhandler method===="+event.getProductId());

        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(event, entity);
        System.out.println("before saving the entity");
        try {
            productRepository.save(entity);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        System.out.println("after saving the entity");
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent){
        logger.info("Product Reserved Event is called for productId : "+productReservedEvent.getProductId() +" and orderId :"+productReservedEvent.getOrderId());
        ProductEntity productEntity = productRepository.findByProductId(productReservedEvent.getProductId());
        productEntity.setQuantity(productEntity.getQuantity()-productReservedEvent.getQuantity());
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent){
        ProductEntity currentlyStoredProduct = productRepository.findByProductId(productReservationCancelledEvent.getProductId());
        int newQuantity = currentlyStoredProduct.getQuantity()+productReservationCancelledEvent.getQuantity();
        currentlyStoredProduct.setQuantity(newQuantity);
        productRepository.save(currentlyStoredProduct);

    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception{
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception){

    }

    @ResetHandler
    public void reset(){
        productRepository.deleteAll();
    }


}
