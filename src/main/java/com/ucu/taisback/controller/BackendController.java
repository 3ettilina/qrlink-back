package com.ucu.taisback.controller;

import com.ucu.taisback.entity.Product;
import com.ucu.taisback.exceptions.ProductNotFoundException;
import com.ucu.taisback.service.FirebaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.ExecutionException;

@RestController
public class BackendController {

  private FirebaseService firebaseService;

  public BackendController(FirebaseService firebaseService){
    this.firebaseService = firebaseService;
  }

  @Value("${TARGET:World}")
  String target;


    @GetMapping("/getProduct")
    RedirectView returnInfoFromQR(@RequestParam String productId) throws InterruptedException, ExecutionException, ProductNotFoundException {
     RedirectView redirectView =  new RedirectView(firebaseService.getProduct(productId).getResource_url());
     redirectView.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
     return redirectView;
    }

  @GetMapping("/admin/product")
  Product getProductInformation(@RequestParam String gtin) throws InterruptedException, ExecutionException, ProductNotFoundException {
    return firebaseService.getProduct(gtin);
  }
}
