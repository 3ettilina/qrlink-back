package com.ucu.taisback.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Product {

  private String gtin;
  private String name;
  private String resource_url;
  private ArrayList<Resource> resources;
}
