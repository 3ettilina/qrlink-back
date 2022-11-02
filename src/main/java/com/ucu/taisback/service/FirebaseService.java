package com.ucu.taisback.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.ucu.taisback.entity.Product;
import com.ucu.taisback.entity.Resource;
import com.ucu.taisback.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

@Service
public class FirebaseService {
  Firestore firestore = FirestoreClient.getFirestore();

  public Product getProduct(String productId) throws ExecutionException, InterruptedException, ProductNotFoundException {
    DocumentReference documentReference = firestore.collection("products").document(productId);
    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
    DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
    return Optional.ofNullable(documentSnapshot)
            .map(documentSnapshot1 -> documentSnapshot1.toObject(Product.class))
            .orElseThrow(() -> new ProductNotFoundException("El codigo del producto no existe!"));
  }

  public void updateResources(String id, Resource resource) throws InterruptedException, ExecutionException, ProductNotFoundException {
    Product product = getProduct(id);
    ArrayList<Resource> resources = Optional.ofNullable(product.getResources())
            .orElse(new ArrayList<>());
   if(resources.stream()
            .filter(resource1 -> Objects.nonNull(resource1.getLanguage()) && Objects.nonNull(resource1.getLink_type()))
            .noneMatch(resource1 -> resource1.getLanguage().equals(resource.getLanguage())
            && resource1.getLink_type().equals(resource.getLink_type()))){
     resources.add(resource);
     product.setResources(resources);
     ApiFuture<WriteResult> writeResultApiFuture = firestore.collection("products").document(id).set(product);
   }
   else{
     //TODO: definir status code y eso
      throw new ProductNotFoundException("El recurso ya existe");
   }

  }
}
