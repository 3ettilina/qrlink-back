package com.ucu.taisback.entity;

import com.google.firebase.database.annotations.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Resource {
  @NotNull
  private String name;
  private String link_type;
  private String language;
  @NotNull
  private String resource_url;
}

