package com.appsdeveloperblog.estore.productservice.command.interceptor;

import com.appsdeveloperblog.estore.productservice.command.CreateProductCommand;
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
    
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> list) {
        return (index, command)->{
            logger.info("intercepted command  :"+command.getPayloadType());
            CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
            if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO)<=0){
                throw new IllegalArgumentException("price cannot be less or equal than zero");
            }
            if(createProductCommand.getTitle()==null || createProductCommand.getTitle().isBlank()){
                throw new IllegalArgumentException("title cannot be emppty");
            }
            return command;
        };
    }
}
