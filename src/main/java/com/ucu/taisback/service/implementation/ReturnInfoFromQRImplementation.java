package com.ucu.taisback.service.implementation;

import com.ucu.taisback.entity.Product;
import com.ucu.taisback.entity.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReturnInfoFromQRImplementation {

  public static Product returnInfoFromQR(Product product,String languageHeader,String linkType){
    Product filteredProduct = new Product();
    List<Resource> resources = product.getResources();

    if(Objects.nonNull(languageHeader)) {
      resources = Optional.ofNullable(product.getResources())
              .orElse(new ArrayList<>())
              .stream()
              .filter(resource -> Objects.nonNull(resource.getLanguage())
                      && resource.getLanguage().equals(languageHeader))
              .collect(Collectors.toList());
    }

    if(Objects.nonNull(linkType)){
      List filteredResources = Optional.ofNullable(resources)
              .orElse(new ArrayList<>())
              .stream()
              .filter(resource -> Objects.nonNull(resource.getLink_type())
                      && resource.getLink_type().equals(linkType))
              .collect(Collectors.toList());
      if (!filteredResources.isEmpty()){
        resources = filteredResources;
      }
    }
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
