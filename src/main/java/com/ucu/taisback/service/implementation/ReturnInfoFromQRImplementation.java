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

    if(!product.isOnly_redirect()){
      if(Objects.nonNull(languageHeader)) {
        resources = filterByLanguageHeader(product, languageHeader);
      }

      if(Objects.nonNull(linkType)){
        resources = filterByLinkType(linkType, resources);
      }

    }
    else{
      resources = filterByDefaultLink(resources);
    }
    buildProduct(product, filteredProduct, resources);
    return filteredProduct;
  }

  private static void buildProduct(Product product, Product filteredProduct, List<Resource> resources) {
    if(!(resources.size() == 0)){
      filteredProduct.setResources((ArrayList<Resource>) resources);
    }
    else{
      filteredProduct.setResources(product.getResources());
    }
    filteredProduct.setGtin(product.getGtin());
    filteredProduct.setOnly_redirect(product.isOnly_redirect());
  }


  private static List<Resource> filterByDefaultLink(List<Resource> resources){
    return Optional.ofNullable(resources)
            .orElse(new ArrayList<>())
            .stream()
            .filter(resource -> resource.getLink_type().equalsIgnoreCase("gs1:defaultLink"))
            .collect(Collectors.toCollection(ArrayList::new));
  }

  private static List<Resource> filterByLinkType(String linkType, List<Resource> resources) {
    List filteredResources = Optional.ofNullable(resources)
            .orElse(new ArrayList<>())
            .stream()
            .filter(resource -> Objects.nonNull(resource.getLink_type())
                    && resource.getLink_type().equals(linkType))
            .collect(Collectors.toList());
    if (!filteredResources.isEmpty()){
      resources = filteredResources;
    }
    return resources;
  }

  private static List<Resource> filterByLanguageHeader(Product product, String languageHeader) {
    return Optional.ofNullable(product.getResources())
            .orElse(new ArrayList<>())
            .stream()
            .filter(resource -> Objects.nonNull(resource.getLanguage())
                    && resource.getLanguage().equals(languageHeader))
            .collect(Collectors.toList());
  }

}
