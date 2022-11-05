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

import java.util.concurrent.ExecutionException;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(BackendController.class)
public class BackendControllerTest extends AbstractTest {

  private final String GET_ENDPOINT = "/getProductResources";

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
    mvc.perform(MockMvcRequestBuilders.get(GET_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Accept-Language","es")
            .param("gtin","123"))
            .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testShouldReturn404WhenIncorrectCodeIsGiven() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get(GET_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
  }

}




