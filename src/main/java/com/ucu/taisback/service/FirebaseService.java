package com.ucu.taisback.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.ucu.taisback.entity.Product;
import com.ucu.taisback.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

  public Product getProduct(String productId) throws ExecutionException, InterruptedException, ProductNotFoundException {
    Firestore firestore = FirestoreClient.getFirestore();
    DocumentReference documentReference = firestore.collection("products").document(productId);
    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
    DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
    return Optional.ofNullable(documentSnapshot)
            .map(documentSnapshot1 -> documentSnapshot1.toObject(Product.class))
            .orElseThrow(() -> new ProductNotFoundException("El codigo del producto no existe!"));
  }
}
