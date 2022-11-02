package com.ucu.taisback.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

  private String gtin;
  private String name;
  private String resource_url;
  private Resource[] resources;
}
