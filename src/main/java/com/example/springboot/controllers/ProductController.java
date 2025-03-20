// package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import jdk.internal.icu.text.UnicodeSet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/product")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> productsList = productRepository.findAll();
        if (productsList.isEmpty()) {
            for (ProductModel productModel : productsList) {
                UUID id = Product.getIdProduct();
                Product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
    }
        return ResponseEntity.status(HttpStatus.OK).body(productsList);

}

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductModel> getOneProduct(@PathVariable(value="id") UUID id){
        Optional<ProductModel> product0 = ProductRepository.findById(id);
        if (product0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        product0.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(product0.get());
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable(value="id") UUID id,
                                                @RequestBody ProductRecordDto productRecordDto){
        Optional<ProductModel> product0 = ProductRepository.findById(id);
        if (product0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        var productModel= product0.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        ProductRepository.save(productModel);
        return ResponseEntity.status(HttpStatus.OK).body("Product updated successfully");
    }
    @DeleteMapping("products/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable(value="id") UUID id){
        Optional<ProductModel> product0 = ProductRepository.findById(id);
        if (product0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        ProductRepository.delete(product0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted suuuccessfully");
}

public void main() {
}


