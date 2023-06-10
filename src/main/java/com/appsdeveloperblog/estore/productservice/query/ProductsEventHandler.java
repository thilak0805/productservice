package com.appsdeveloperblog.estore.productservice.query;

import com.appsdeveloperblog.estore.productservice.core.data.ProductEntity;
import com.appsdeveloperblog.estore.productservice.core.data.ProductRepository;
import com.appsdeveloperblog.estore.productservice.core.events.ProductCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductsEventHandler {
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
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("after saving the entity");

    }


}
