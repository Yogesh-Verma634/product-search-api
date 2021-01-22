package com.myretail.productsearchapi.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.productsearchapi.controller.ProductSearchController;
import com.myretail.productsearchapi.dto.ProductPriceDto;
import com.myretail.productsearchapi.mapper.ProductPriceMapper;
import com.myretail.productsearchapi.model.Price;
import com.myretail.productsearchapi.model.Product;
import com.myretail.productsearchapi.repository.PriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
public class ProductSearchControllerImpl implements ProductSearchController {

    private static final Logger logger = LoggerFactory.getLogger(ProductSearchController.class);

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final static String API_URL = "https://redsky.target.com/v3/pdp/tcin/";

    private final static String EXCLUDE_API_PARAMETERS = "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate#_blank";

    @Override
    public ResponseEntity<ProductPriceDto> getProductDetails(Integer id) throws HttpClientErrorException.BadRequest, JsonProcessingException, HttpClientErrorException.NotFound {

        String url = API_URL + id.toString() + EXCLUDE_API_PARAMETERS;

        Optional<Price> price = priceRepository.findById(id);

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            if (responseEntity.hasBody()) {

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseEntity.getBody());
                JsonNode name = root.path("product");

                Product product = new Product();
                product.setId(name.get("available_to_promise_network").get("product_id").asLong());
                product.setDescription(name.get("item").get("product_description").get("downstream_description").toString());
                product.setName(name.get("item").get("product_description").get("title").toString());

                if (price.isPresent()) {
                    ProductPriceDto productPriceDto = ProductPriceMapper.instance.map(product, price.get());
                    return ResponseEntity.ok(productPriceDto);
                }
                return ResponseEntity.notFound().build();
            }
        }
        catch (HttpServerErrorException.InternalServerError e){
            logger.error("Product not found.");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> add(ProductPriceDto productPrice) throws HttpClientErrorException.BadRequest {
        logger.info("Adding a product to database with id: {}", productPrice.getId());
        priceRepository.save(ProductPriceMapper.instance.productPriceToPrice(productPrice));
        return ResponseEntity.ok("Successfully saved the product");
    }

    @Override
    public ResponseEntity<String> modify(ProductPriceDto productPrice) throws HttpClientErrorException.BadRequest {
        logger.info("Updating product information to an id: {}", productPrice.getId());
        Optional<Price> priceOptional = priceRepository.findById(productPrice.getId());

        if(priceOptional.isPresent()){
            priceRepository.save(ProductPriceMapper.instance.productPriceToPrice(productPrice));
            return ResponseEntity.ok("Successfully updated the product information for an ID");
        }

        logger.info("Product not found. Please create it first");
        return ResponseEntity.notFound().build();
    }
}
