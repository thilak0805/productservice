package com.appsdeveloperblog.estore.productservice.command;

import com.appsdeveloperblog.estore.productservice.core.data.ProductLookupEntity;
import com.appsdeveloperblog.estore.productservice.core.data.ProductLookupRepository;
import com.appsdeveloperblog.estore.productservice.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
// for the above processing group, axon framework will create a separate
// own tracking event processor. The tracking event processor will use
// special tokens which will be used to avoid multiple processing of the
// same event in different threads and nodes.
//by using this processin group, be grouping these event handlers in same logical
// group. we will make sure, that events are handled only once and they are
// handled in same thread. this gives us possibility to rollback the whole
// transactions if event processor is not sucessful.
public class ProductsLookupEventsHandler {

    private final ProductLookupRepository productLookupRepository;

    public ProductsLookupEventsHandler(ProductLookupRepository productLookupRepository){
        this.productLookupRepository = productLookupRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){

        ProductLookupEntity productLookupEntity = new ProductLookupEntity(event.getProductId(), event.getTitle());

        productLookupRepository.save(productLookupEntity);
    }

    @ResetHandler
    public void reset(){
        productLookupRepository.deleteAll();
    }
}
