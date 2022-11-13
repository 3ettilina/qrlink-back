package com.ucu.taisback.service.implementation;

import com.ucu.taisback.entity.Product;
import com.ucu.taisback.entity.Resource;

import java.util.ArrayList;

public class AddProductImplementation {

  public static Product buildProduct(Product product){
    Resource resource = new Resource();
    resource.setName(product.getName());
    resource.setResource_url(product.getResource_url());
    resource.setLink_type(product.getLink_type());

    ArrayList<Resource> resources = new ArrayList<>();
    resources.add(resource);

    product.setResources(resources);

    return product;

  }
}
