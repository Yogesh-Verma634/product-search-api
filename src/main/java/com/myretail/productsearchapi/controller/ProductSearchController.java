package com.myretail.productsearchapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myretail.productsearchapi.dto.ProductPriceDto;
import com.myretail.productsearchapi.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RequestMapping("/products")
@Api(produces = "application/json", tags = {"Product Search Controller"})
public interface ProductSearchController {

    @GetMapping("/{id}")
    @ApiOperation(value = "Retrieve product information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved product information", response = Product.class),
            @ApiResponse(code = 404,  message = "Product Not Found")
    }
    )
    ResponseEntity<ProductPriceDto> getProductDetails(@PathVariable Integer id) throws HttpClientErrorException.BadRequest, JsonProcessingException;

    @PostMapping("/")
    @ApiOperation(value = "Add a product to the store")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added a product to an store", response = Product.class)
    }
    )
    ResponseEntity<String> add(@RequestBody ProductPriceDto productPrice) throws HttpClientErrorException.BadRequest;

    @PutMapping("/")
    @ApiOperation(value = "Modify a product information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully modify product information", response = void.class)
    }
    )
    ResponseEntity<String> modify(@RequestBody ProductPriceDto productPrice) throws HttpClientErrorException.BadRequest;

}
