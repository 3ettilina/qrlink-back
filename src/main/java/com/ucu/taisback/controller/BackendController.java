package com.ucu.taisback.controller;

import com.google.common.net.HttpHeaders;
import com.ucu.taisback.entity.Product;
import com.ucu.taisback.entity.Resource;
import com.ucu.taisback.exceptions.ProductNotFoundException;
import com.ucu.taisback.service.FirebaseService;
import com.ucu.taisback.service.implementation.ReturnInfoFromQRImplementation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class BackendController {

  private FirebaseService firebaseService;

  public BackendController(FirebaseService firebaseService){
    this.firebaseService = firebaseService;
  }

    @GetMapping("/getProductResources")
    Product returnInfoFromQR(@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String languageHeader,
            @RequestParam String gtin) throws InterruptedException, ExecutionException, ProductNotFoundException {
     return ReturnInfoFromQRImplementation.returnInfoFromQR(firebaseService.getProduct(gtin),languageHeader);
    }

    @GetMapping("/admin/product")
    Product getProductInformation(@RequestParam String gtin) throws InterruptedException, ExecutionException, ProductNotFoundException {
      return firebaseService.getProduct(gtin);
    }

    @PostMapping("/product/addResource")
    void addResource(@RequestParam String gtin,  @RequestBody Resource resource) throws InterruptedException, ExecutionException, ProductNotFoundException {
      firebaseService.updateResources(gtin, resource);
    }
}
