package com.ucu.taisback.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.ucu.taisback.entity.Product;
import com.ucu.taisback.entity.Resource;
import com.ucu.taisback.entity.LinkType;
import com.ucu.taisback.exceptions.ProductNotFoundException;
import com.ucu.taisback.service.implementation.AddProductImplementation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class FirebaseService {
  Firestore firestore = FirestoreClient.getFirestore();

  public Product getProduct(String gtin) throws ExecutionException, InterruptedException, ProductNotFoundException {
    DocumentReference documentReference = firestore.collection("products").document(gtin);
    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
    DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
    return Optional.ofNullable(documentSnapshot)
            .map(documentSnapshot1 -> documentSnapshot1.toObject(Product.class))
            .orElseThrow(() -> new ProductNotFoundException("El codigo del producto no existe!"));
  }

  public void updateResources(String id, Resource resource) throws InterruptedException, ExecutionException, ProductNotFoundException {
    if(Objects.nonNull(resource.getName())){
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
    else{
      throw new ProductNotFoundException("Resource mal formado");

    }
  }

  public Product addProduct(Product product) throws ExecutionException, InterruptedException, ProductNotFoundException {
    if(Objects.nonNull(product.getResources().get(0).getName())){
      DocumentReference documentReference = firestore.collection("products").document(product.getGtin());
      ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
      Product documentSnapshot = documentSnapshotApiFuture.get().toObject(Product.class);
      if(Objects.isNull(documentSnapshot)){
        AddProductImplementation.buildProduct(product);
        firestore.collection("products").document(product.getGtin()).set(product);
        return product;
      }
      else{
        throw new ProductNotFoundException("el producto ya existe");
      }
    }
    else{
      throw new ProductNotFoundException("producto tiene resource mal formado");
    }

  }

  public Resource editResource(Resource resource, String gtin) throws InterruptedException, ExecutionException, ProductNotFoundException {
    Product product = getProduct(gtin);
    ArrayList<Resource> resources = buildResources(product, resource);
    product.setResources(resources);
    ApiFuture<WriteResult> collectionsApiFuture =
            firestore.collection("products").document(product.getGtin()).set(product);
    return resource;
  }

  public List<LinkType> getAllLinkTypes() throws ExecutionException, InterruptedException {
    CollectionReference documentReference = firestore.collection("link_types");
    ApiFuture<QuerySnapshot> documentSnapshotApiFuture = documentReference.get();
    QuerySnapshot documentSnapshot = documentSnapshotApiFuture.get();

    return   documentSnapshot.getDocuments().stream()
            .map(this::buildLinkType)
            .collect(Collectors.toList());
  }

  public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
    CollectionReference documentReference = firestore.collection("products");
    ApiFuture<QuerySnapshot> documentSnapshotApiFuture = documentReference.get();
    QuerySnapshot documentSnapshot = documentSnapshotApiFuture.get();

    return   documentSnapshot.getDocuments().stream()
            .map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(Product.class))
            .collect(Collectors.toList());
  }

  public void deleteProduct(String gtin) throws InterruptedException, ExecutionException, ProductNotFoundException {
    getProduct(gtin); //para chequear que existe
    firestore.collection("products").document(gtin).delete();
  }

  public Product switchRedirect(String gtin) throws InterruptedException, ExecutionException, ProductNotFoundException {
    Product product = getProduct(gtin);
    product.setOnly_redirect(!product.isOnly_redirect());
    firestore.collection("products").document(gtin).set(product);
    return product;
  }

  public void deleteResource(String gtin,Resource resource) throws InterruptedException, ExecutionException, ProductNotFoundException {
    Product product = getProduct(gtin);
    ArrayList<Resource> resources = product.getResources()
            .stream()
            .filter(r -> !matcher(resource,r) )
            .collect(Collectors.toCollection(ArrayList::new));

    product.setResources(resources);
    firestore.collection("products").document(product.getGtin()).set(product);

  }

  private LinkType buildLinkType(QueryDocumentSnapshot queryDocumentSnapshot){
    String id = queryDocumentSnapshot.getId();
    LinkType linkType = queryDocumentSnapshot.toObject(LinkType.class);
    linkType.setId(id);
    return linkType;
  }

  private ArrayList<Resource> buildResources(Product product,Resource resource) throws ProductNotFoundException {
    if (Optional.ofNullable(product.getResources())
            .orElse(new ArrayList<>())
            .stream()
            .anyMatch(r ->editResourceMatcher(r,resource)) ){

      ArrayList<Resource> resources=
       product.getResources()
              .stream()
              .filter(r ->!editResourceMatcher(r,resource) )
              .collect(Collectors.toCollection(ArrayList::new));
      resources.add(resource);
      return resources;
    }
    else{
      throw new ProductNotFoundException("No se encontro el recurso");
    }
  }

  protected boolean matcher(Resource resource, Resource newResource){
    BiFunction<String, String, Boolean> matchByField = (s1,s2) -> {
     if (Objects.isNull(s1) && Objects.isNull(s2)){
       return true;
     }
     else if( Objects.nonNull(s1) && Objects.nonNull(s2) && s1.equals(s2)){
         return true;
       }
     else
       return false;
     };

    return matchByField.apply(resource.getLanguage(),newResource.getLanguage())
            && matchByField.apply(resource.getLink_type(),newResource.getLink_type())
            && matchByField.apply(resource.getName(),newResource.getName())
            && matchByField.apply(resource.getResource_url(),newResource.getResource_url());
  }

  protected boolean editResourceMatcher(Resource resource, Resource newResource){
    BiFunction<String, String, Boolean> matchByField = (s1,s2) -> {
      if (Objects.isNull(s1) && Objects.isNull(s2)){
        return true;
      }
      else if( Objects.nonNull(s1) && Objects.nonNull(s2) && s1.equals(s2)){
        return true;
      }
      else
        return false;
    };

    return matchByField.apply(resource.getLanguage(),newResource.getLanguage())
            && matchByField.apply(resource.getLink_type(),newResource.getLink_type())
            && matchByField.apply(resource.getName(),newResource.getName());
  }
}
