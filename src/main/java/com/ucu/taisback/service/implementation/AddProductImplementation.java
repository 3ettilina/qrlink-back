package com.ucu.taisback.service.implementation;

import com.ucu.taisback.entity.Product;
import com.ucu.taisback.entity.Resource;

import java.util.ArrayList;

public class AddProductImplementation {

  public static final String DEFAULT_LINK = "gs1:defaultLink";

  public static Product buildProduct(Product product){

    product.getResources().get(0).setLink_type(DEFAULT_LINK);

    return product;

  }
}
