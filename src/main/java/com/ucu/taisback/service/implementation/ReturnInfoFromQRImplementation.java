package com.ucu.taisback.service.implementation;

import com.ucu.taisback.entity.Product;
import com.ucu.taisback.entity.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ReturnInfoFromQRImplementation {

  public static Product returnInfoFromQR(Product product,String languageHeader){
    Product filteredProduct = new Product();

    List<Resource> resources =  Optional.ofNullable(product.getResources())
          .orElse(new ArrayList<>())
            .stream()
            .filter(resource -> Objects.nonNull(resource.getLanguage()) && resource.getLanguage().equals(languageHeader))
            .collect(Collectors.toList());


    if(!(resources.size() == 0)){
      filteredProduct.setResources((ArrayList<Resource>) resources);
    }
    else{
      filteredProduct.setResources(product.getResources());
    }
    filteredProduct.setGtin(product.getGtin());
    filteredProduct.setResource_url(product.getResource_url());
    filteredProduct.setName(product.getName());

    return filteredProduct;
  }

}
