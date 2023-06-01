package com.appsdeveloperblog.estore.productservice;

import com.appsdeveloperblog.estore.productservice.rest.CreateProductRestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private Environment env;

    @PostMapping
    public String createProduct(@RequestBody CreateProductRestModel createProductRestModel){

        return "Http post method "+createProductRestModel.getTitle();
    }




    
    @GetMapping
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
    }
}
