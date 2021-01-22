package com.myretail.productsearchapi.repository;

import com.myretail.productsearchapi.model.Price;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceRepository extends MongoRepository<Price, Integer> {
}
