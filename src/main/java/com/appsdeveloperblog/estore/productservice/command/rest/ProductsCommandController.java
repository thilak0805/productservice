package com.appsdeveloperblog.estore.productservice.command.rest;

import com.appsdeveloperblog.estore.productservice.command.CreateProductCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductsCommandController {

    @Autowired
    private Environment env;

    private final CommandGateway commandGateway;

    @Autowired
    public ProductsCommandController(Environment env, CommandGateway commandGateway){
        this.env = env;
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createProduct(@RequestBody CreateProductRestModel createProductRestModel){
        String returnValue = "";
        CreateProductCommand createProductCommand = CreateProductCommand.builder().price(createProductRestModel.getPrice())
                .quantity(createProductRestModel.getQuantity())
                .title(createProductRestModel.getTitle())
                .productId(UUID.randomUUID().toString()).build();
        try {               //commandobj           routes commandobj to
            //commandGateway------------>commandbus--------------------> CommandHandler
            returnValue = commandGateway.sendAndWait(createProductCommand);
        }catch(Exception e){
           returnValue = e.getLocalizedMessage();
        }
       // return "Http post method "+createProductRestModel.getTitle();
        return returnValue;
    }




    
    /*@GetMapping
    public String getProduct(){
        return "Http get method "+env.getProperty("local.server.port");
    }

    @PutMapping
    public String updateProduct(){
        return "Http put method";
    }

    @DeleteMapping
    public String deleteProduct(){
        return "Http delete method";
    }*/
}
