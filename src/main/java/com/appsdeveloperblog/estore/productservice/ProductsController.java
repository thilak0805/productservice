package com.appsdeveloperblog.estore.productservice;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @PostMapping
    public String createProduct(){
        return "Http post method";
    }




    
    @GetMapping
    public String getProduct(){
        return "Http get method";
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
