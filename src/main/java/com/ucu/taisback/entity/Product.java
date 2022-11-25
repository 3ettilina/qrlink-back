package com.ucu.taisback.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.validation.OverridesAttribute;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

@Getter
@Setter
public class Product {

  private String gtin;
  @NotEmpty
  private ArrayList<Resource> resources;
  private boolean only_redirect;

  @SneakyThrows
  @Override
  public String toString(){
    ObjectMapper Obj = new ObjectMapper();
    String jsonStr = Obj.writeValueAsString(this);
    return jsonStr;
  }
}
