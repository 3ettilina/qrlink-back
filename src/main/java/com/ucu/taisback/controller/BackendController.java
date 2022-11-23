package com.ucu.taisback.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import com.ucu.taisback.entity.LinkType;
import com.ucu.taisback.entity.Product;
import com.ucu.taisback.entity.Resource;
import com.ucu.taisback.exceptions.ProductNotFoundException;
import com.ucu.taisback.service.FirebaseService;
import com.ucu.taisback.service.implementation.ReturnInfoFromQRImplementation;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class BackendController {

  private FirebaseService firebaseService;
  MultiValueMap<String, String> headers = new HttpHeaders();

  public BackendController(FirebaseService firebaseService){
    this.firebaseService = firebaseService;
  }

    @GetMapping("/getProductResources")
    Product returnInfoFromQR(@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false)
                                                 String languageHeader,HttpServletResponse response,
                                              @RequestParam String gtin,
                                              @RequestParam (required = false)String linkType)
            throws InterruptedException, ExecutionException, ProductNotFoundException {
      Product product = ReturnInfoFromQRImplementation.returnInfoFromQR(firebaseService.getProduct(gtin),languageHeader, linkType);
      response.setHeader("product",product.toString());
      return product;
    }

    @GetMapping("/admin/product")
    Product getProductInformation(@RequestParam String gtin) throws InterruptedException, ExecutionException, ProductNotFoundException {
      return firebaseService.getProduct(gtin);
    }

    @PostMapping("/product/addResource")
    void addResource(@RequestParam String gtin,  @RequestBody Resource resource) throws InterruptedException, ExecutionException, ProductNotFoundException {
      firebaseService.updateResources(gtin, resource);
    }

    @PostMapping("/product/addProduct")
    Product addResource( @RequestBody Product product) throws InterruptedException, ExecutionException, ProductNotFoundException {
      return firebaseService.addProduct(product);
    }

    @GetMapping("/admin/getLinkTypeList")
    List<LinkType> getProductInformation() throws InterruptedException, ExecutionException {
      return firebaseService.getAllLinkTypes();
    }

    @PatchMapping("/product/editResource")
    Resource editProduct(@RequestParam String gtin,
                         @RequestBody Resource resource) throws InterruptedException, ExecutionException, ProductNotFoundException {
      return firebaseService.editResource(resource,gtin);
    }

    @DeleteMapping("/product/delete")
    void deleteProduct(@RequestParam String gtin) throws InterruptedException, ExecutionException, ProductNotFoundException {
      firebaseService.deleteProduct(gtin);
    }

  @GetMapping("/product/getProducts")
  List<Product> getProducts() throws InterruptedException, ExecutionException {
    return firebaseService.getAllProducts();
  }

  @PostMapping("/product/setOnlyRedirect")
  Product switchRedirect( @RequestParam String gtin) throws InterruptedException, ExecutionException, ProductNotFoundException {
    return firebaseService.switchRedirect(gtin);
  }

  @DeleteMapping("/product/deleteResource")
  void deleteResource(@RequestParam String gtin,  @RequestBody Resource resource) throws InterruptedException, ExecutionException, ProductNotFoundException {
    firebaseService.deleteResource(gtin, resource);
  }

}
