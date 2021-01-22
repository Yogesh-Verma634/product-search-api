package com.myretail.productsearchapi.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    public Long id;

    public String name;

    public String description;
}
