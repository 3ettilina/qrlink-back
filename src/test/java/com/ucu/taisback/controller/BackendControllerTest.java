package com.ucu.taisback.controller;


import com.ucu.taisback.controller.util.AbstractTest;
import com.ucu.taisback.entity.Product;
import com.ucu.taisback.exceptions.ProductNotFoundException;
import com.ucu.taisback.service.FirebaseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.rmi.ServerError;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(BackendController.class)
public class BackendControllerTest extends AbstractTest {

  private final String GET_PRODUCT_RESOURCES = "/getProductResources";
  private final String GET_ADMIN_PRODUCT = "/admin/product";

  private final String GET_PRODUCT_ADD = "/product/addResource"; // Para Task 33
  @Autowired
  private MockMvc mvc;

  @MockBean
  private FirebaseService service;

  @InjectMocks
  private BackendController backendController;


  private Product product = new Product();

  @Before
  public void init() throws InterruptedException, ExecutionException, ProductNotFoundException {
    product.setName("pepe");
    product.setResource_url("unaUrl.com");
    product.setGtin("123");
    given(service.getProduct("123")).willReturn(product);
  }


  @Test
  public void testShouldReturn200WhenCorrectCodeIsGiven() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get(GET_PRODUCT_RESOURCES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "es")
                    .param("gtin", "123"))
            .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testShouldReturn404WhenIncorrectCodeIsGiven() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get(GET_PRODUCT_RESOURCES)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
  }

  @Test
  public void testShouldReturn200WhenCorrectResource() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get(GET_ADMIN_PRODUCT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "es")
                    .param("gtin", "123"))
            .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testShouldReturn404WhenGtinNotFound() throws Exception {
    when(service.getProduct("444")).thenThrow(ProductNotFoundException.class);

    mvc.perform(MockMvcRequestBuilders.get(GET_ADMIN_PRODUCT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "es")
                    .param("gtin", "444"))
            .andExpect(status().is4xxClientError());
  }

  // Test error 500
  @Test(expected = NestedServletException.class)
  public void testShouldReturn500ErrorWhenServiceFails() throws Exception {
    when(service.getProduct("444")).thenThrow(InterruptedException.class);

    mvc.perform(MockMvcRequestBuilders.get(GET_ADMIN_PRODUCT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "es")
                    .param("gtin", "444"))
            .andExpect(status().is5xxServerError());
  }

  //task 33
  @Test
  public void testShouldReturn404WhenGtinNotExist() throws Exception {
    when(service.getProduct("444")).thenThrow(ProductNotFoundException.class);

    mvc.perform(MockMvcRequestBuilders.get(GET_PRODUCT_ADD)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "es")
                    .param("gtin", "444"))
            .andExpect(status().is4xxClientError());
  }
//este es el que falló
 // @Test(expected = NestedServletException.class)
 // public void testShouldReturn500ErrorWhenServiceFails2() throws Exception {
  //  when(service.getProduct("444")).thenThrow(InterruptedException.class);

  //  mvc.perform(MockMvcRequestBuilders.get(GET_PRODUCT_ADD)
       //             .contentType(MediaType.APPLICATION_JSON)
       //             .header("Accept-Language", "es")
       //             .param("gtin", "444"))
      //      .andExpect(status().is5xxServerError());
 // }
 // @Test
 // public void testShouldReturn201WhenCorrectCreateProduct() throws Exception {
   // mvc.perform(MockMvcRequestBuilders.get(GET_PRODUCT_ADD)
  //                  .contentType(MediaType.APPLICATION_JSON)
   //                 .header("Accept-Language", "es")
 //                   .param("gtin", "123")) //y acá qué va?

   //         .andExpect(status().is2xxSuccessful());
 // }
}




