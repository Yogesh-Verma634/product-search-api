package com.myretail.productsearchapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceDto {

    public Integer id;

    public String name;

    public String description;

    public double value;

    public String currency_code;
}
