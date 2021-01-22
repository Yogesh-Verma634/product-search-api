package com.myretail.productsearchapi.mapper;

import com.myretail.productsearchapi.dto.ProductPriceDto;
import com.myretail.productsearchapi.model.Price;
import com.myretail.productsearchapi.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductPriceMapper {

    ProductPriceMapper instance = Mappers.getMapper(ProductPriceMapper.class);

    @Mapping(source = "product.id", target = "id")
    ProductPriceDto map(Product product, Price price);


    @Mapping(source = "productPrice.id", target = "id")
    @Mapping(source = "productPrice.value", target = "value")
    @Mapping(source = "productPrice.currency_code", target = "currency_code")
    Price productPriceToPrice(ProductPriceDto productPrice);
}
