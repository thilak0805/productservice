package com.appsdeveloperblog.estore.productservice.command.interceptor;

import com.appsdeveloperblog.estore.productservice.command.CreateProductCommand;
import com.appsdeveloperblog.estore.productservice.core.data.ProductLookupEntity;
import com.appsdeveloperblog.estore.productservice.core.data.ProductLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    
    private static final Logger logger = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);

    private final ProductLookupRepository productLookupRepository;

    public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository){
        this.productLookupRepository = productLookupRepository;
    }
    
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> list) {
        return (index, command)->{
            logger.info("intercepted command  :"+command.getPayloadType());
            CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
            /*if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO)<=0){
                throw new IllegalArgumentException("price cannot be less or equal than zero");
            }
            if(createProductCommand.getTitle()==null || createProductCommand.getTitle().isBlank()){
                throw new IllegalArgumentException("title cannot be emppty");
            }*/

            ProductLookupEntity productLookupEntity =
                    productLookupRepository.findByProductIdOrTitle(createProductCommand.getProductId(), createProductCommand.getTitle());
            if(productLookupEntity!=null){
                throw new IllegalArgumentException(String.format("Product with product Id %s or title already exists",
                        createProductCommand.getProductId(), createProductCommand.getTitle()));
            }

            return command;
        };
    }
}
