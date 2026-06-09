package com.project.code.Controller;


import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    @PostMapping
    public ResponseEntity<Map<String, String>> addProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();
        try{
            if (!serviceClass.validateProduct(product)){
                response.put("message", "Product already exists in the database");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            productRepository.save(product);
            response.put("message", "Product has been saved successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataIntegrityViolationException ex) {
            response.put("message", "Database Error: Data integrity violation. Duplicate SKU or unique key constraint violated");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        catch (Exception e){
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();

        try{
            if(!serviceClass.validateProductId(id)){
                response.put("message", "Product id does not exist");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Optional<Product> product = productRepository.findById(id);
            response.put("products", product);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<Map<String, String>> updateProduct(@PathVariable Long productId, @RequestBody Product product){
        Map<String, String> response = new HashMap<>();
        try{
            if(!serviceClass.validateProductId(productId)){
                response.put("message", "Product id does not exist");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            product.setId(productId);
            productRepository.save(product);
            response.put("message", "Product has been updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/category/{name}/{category}")
    public ResponseEntity<Map<String, Object>> filterByCategoryProduct (@PathVariable String name, @PathVariable String category){
        Map<String, Object> response = new HashMap<>();
        try{
            List<Product> productsList = new ArrayList<>();
            if (name == null || name.equalsIgnoreCase("null")){
                 productsList = productRepository.findByCategory(category);
            } else if (category == null || category.equalsIgnoreCase("null")){
                Optional<Product> product = productRepository.findByName(name);
                if (product.isPresent()){
                    productsList.add(product.get());
                }
            } else {
                productsList = productRepository.findProductBySubNameAndCategory(name, category);
            }

            response.put("products", productsList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listProduct(){
        Map<String, Object> response = new HashMap<>();
        try{
            List<Product> productsList = productRepository.findAll();
            response.put("products", productsList);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id){
        Map<String, String> response = new HashMap<>();
        try{
            if(!serviceClass.validateProductId(id)){
                response.put("message", "Product id does not exist");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            inventoryRepository.deleteByProductId(id);
            productRepository.deleteById(id);
            response.put("message", "Product has been deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/searchProduct/{name}")
    public ResponseEntity<Map<String, Object>> searchProduct(@PathVariable String name){
        Map<String, Object> response = new HashMap<>();
        try{
            List<Product> productList = productRepository.findProductBySubName(name);
            response.put("products", productList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to designate it as a REST controller for handling HTTP requests.
//    - Map the class to the `/product` URL using `@RequestMapping("/product")`.


// 2. Autowired Dependencies:
//    - Inject the following dependencies via `@Autowired`:
//        - `ProductRepository` for CRUD operations on products.
//        - `ServiceClass` for product validation and business logic.
//        - `InventoryRepository` for managing the inventory linked to products.


// 3. Define the `addProduct` Method:
//    - Annotate with `@PostMapping` to handle POST requests for adding a new product.
//    - Accept `Product` object in the request body.
//    - Validate product existence using `validateProduct()` in `ServiceClass`.
//    - Save the valid product using `save()` method of `ProductRepository`.
//    - Catch exceptions (e.g., `DataIntegrityViolationException`) and return appropriate error message.


// 4. Define the `getProductbyId` Method:
//    - Annotate with `@GetMapping("/product/{id}")` to handle GET requests for retrieving a product by ID.
//    - Accept product ID via `@PathVariable`.
//    - Use `findById(id)` method from `ProductRepository` to fetch the product.
//    - Return the product in a `Map<String, Object>` with key `products`.


 // 5. Define the `updateProduct` Method:
//    - Annotate with `@PutMapping` to handle PUT requests for updating an existing product.
//    - Accept updated `Product` object in the request body.
//    - Use `save()` method from `ProductRepository` to update the product.
//    - Return a success message with key `message` after updating the product.


// 6. Define the `filterbyCategoryProduct` Method:
//    - Annotate with `@GetMapping("/category/{name}/{category}")` to handle GET requests for filtering products by `name` and `category`.
//    - Use conditional filtering logic if `name` or `category` is `"null"`.
//    - Fetch products based on category using methods like `findByCategory()` or `findProductBySubNameAndCategory()`.
//    - Return filtered products in a `Map<String, Object>` with key `products`.


 // 7. Define the `listProduct` Method:
//    - Annotate with `@GetMapping` to handle GET requests to fetch all products.
//    - Fetch all products using `findAll()` method from `ProductRepository`.
//    - Return all products in a `Map<String, Object>` with key `products`.


// 8. Define the `getProductbyCategoryAndStoreId` Method:
//    - Annotate with `@GetMapping("filter/{category}/{storeid}")` to filter products by `category` and `storeId`.
//    - Use `findProductByCategory()` method from `ProductRepository` to retrieve products.
//    - Return filtered products in a `Map<String, Object>` with key `product`.


// 9. Define the `deleteProduct` Method:
//    - Annotate with `@DeleteMapping("/{id}")` to handle DELETE requests for removing a product by its ID.
//    - Validate product existence using `ValidateProductId()` in `ServiceClass`.
//    - Remove product from `Inventory` first using `deleteByProductId(id)` in `InventoryRepository`.
//    - Remove product from `Product` using `deleteById(id)` in `ProductRepository`.
//    - Return a success message with key `message` indicating product deletion.


 // 10. Define the `searchProduct` Method:
//    - Annotate with `@GetMapping("/searchProduct/{name}")` to search for products by `name`.
//    - Use `findProductBySubName()` method from `ProductRepository` to search products by name.
//    - Return search results in a `Map<String, Object>` with key `products`.


  
    
}
