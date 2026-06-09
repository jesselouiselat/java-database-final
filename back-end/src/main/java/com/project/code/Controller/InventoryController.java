package com.project.code.Controller;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Model.Store;
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
@RequestMapping("/inventory")



public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceClass serviceClass;

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateInventory(@PathVariable Long id,
                               @RequestBody CombinedRequest combinedRequest){
        Map<String, String> response = new HashMap<>();
        try{
            Product productInput = combinedRequest.getProduct();
            Inventory inventoryInput = combinedRequest.getInventory();

            if(!serviceClass.validateProductId(productInput.getId())){
                response.put("message", "Product Id is invalid with ID "+productInput.getId());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            Inventory existingInventory = serviceClass.getInventoryId(inventoryInput);

            if(existingInventory != null){
                existingInventory.setStockLevel(inventoryInput.getStockLevel());
                inventoryRepository.save(existingInventory);

                response.put("message", "Inventory has been updated successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "No data available with ID "+productInput.getId());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e){
            response.put("message","An error has occurred:" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity<Map<String, String>> saveInventory(@RequestBody Inventory inventory){
        Map<String, String> response = new HashMap<>();

        try{
            if(!serviceClass.validateInventory(inventory)){
                response.put("message", "Inventory is already in the database "+inventory.getId());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            inventoryRepository.save(inventory);
            response.put("message", "Inventory has been saved successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataIntegrityViolationException e) {
            response.put("message", "Database Error:Data integrity violation. Verify product and store associations.");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        catch(Exception e){
            response.put("message","An error has occurred:" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<Map<String, Object>> getAllProducts(@PathVariable Long storeId){
        Map<String, Object> response = new HashMap<>();
        try{
            List<Product> productList =  productRepository.findProductByStoreId(storeId);
            response.put("products", productList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e){
            response.put("message","An error has occurred:" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter/{category}/{name}/{storeId}")
    public ResponseEntity<Map<String, Object>> getProductName(@PathVariable String category, @PathVariable String name, @PathVariable Long storeId){
        Map<String, Object> response = new HashMap<>();
        try{
            List<Product> productList;
          if(category == null || category.equalsIgnoreCase("null")){
                productList = productRepository.findByNameLike(storeId, name);
          } else if(name == null || name.equalsIgnoreCase("null")){
              productList = productRepository.findByCategoryAndStoreId(storeId, category);
          } else {
              productList = productRepository.findByNameAndCategory(storeId, name, category);
          }
            response.put("product", productList);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch(Exception e){
            response.put("message","An error has occurred:" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/search/{name}/{storeId}")
    public ResponseEntity<Map<String, Object>> searchProduct(@PathVariable String name, @PathVariable Long storeId){
        Map<String, Object> response = new HashMap<>();
        try{
            List<Product> productList;
            productList = productRepository.findByNameLike(storeId, name);
            response.put("product", productList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e){
            response.put("message","An error has occurred:" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> removeProduct(@PathVariable Long id){
        Map<String, String> response = new HashMap<>();
        try{
            if(!serviceClass.validateProductId(id)){
                response.put("message", "product not present in database");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            inventoryRepository.deleteByProductId(id);
            productRepository.deleteById(id);

            response.put("message", "Product and its corresponding inventory have been deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e){
            response.put("message","An error has occurred:" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validate/{quantity}/{storeId}/{productId}")
    public ResponseEntity<Boolean> validateQuantity(@PathVariable Long quantity, @PathVariable Long storeId, @PathVariable Long productId){
        try{
            Inventory inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);
            if(inventory == null){
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }

            if (inventory.getStockLevel()>=quantity){
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else
                return new ResponseEntity<>(false, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to indicate that this is a REST controller, which handles HTTP requests and responses.
//    - Use `@RequestMapping("/inventory")` to set the base URL path for all methods in this controller. All endpoints related to inventory will be prefixed with `/inventory`.


// 2. Autowired Dependencies:
//    - Autowire necessary repositories and services:
//      - `ProductRepository` will be used to interact with product data (i.e., finding, updating products).
//      - `InventoryRepository` will handle CRUD operations related to the inventory.
//      - `ServiceClass` will help with the validation logic (e.g., validating product IDs and inventory data).


// 3. Define the `updateInventory` Method:
//    - This method handles HTTP PUT requests to update inventory for a product.
//    - It takes a `CombinedRequest` (containing `Product` and `Inventory`) in the request body.
//    - The product ID is validated, and if valid, the inventory is updated in the database.
//    - If the inventory exists, update it and return a success message. If not, return a message indicating no data available.


// 4. Define the `saveInventory` Method:
//    - This method handles HTTP POST requests to save a new inventory entry.
//    - It accepts an `Inventory` object in the request body.
//    - It first validates whether the inventory already exists. If it exists, it returns a message stating so. If it doesn’t exist, it saves the inventory and returns a success message.


// 5. Define the `getAllProducts` Method:
//    - This method handles HTTP GET requests to retrieve products for a specific store.
//    - It uses the `storeId` as a path variable and fetches the list of products from the database for the given store.
//    - The products are returned in a `Map` with the key `"products"`.


// 6. Define the `getProductName` Method:
//    - This method handles HTTP GET requests to filter products by category and name.
//    - If either the category or name is `"null"`, adjust the filtering logic accordingly.
//    - Return the filtered products in the response with the key `"product"`.


// 7. Define the `searchProduct` Method:
//    - This method handles HTTP GET requests to search for products by name within a specific store.
//    - It uses `name` and `storeId` as parameters and searches for products that match the `name` in the specified store.
//    - The search results are returned in the response with the key `"product"`.


// 8. Define the `removeProduct` Method:
//    - This method handles HTTP DELETE requests to delete a product by its ID.
//    - It first validates if the product exists. If it does, it deletes the product from the `ProductRepository` and also removes the related inventory entry from the `InventoryRepository`.
//    - Returns a success message with the key `"message"` indicating successful deletion.


// 9. Define the `validateQuantity` Method:
//    - This method handles HTTP GET requests to validate if a specified quantity of a product is available in stock for a given store.
//    - It checks the inventory for the product in the specified store and compares it to the requested quantity.
//    - If sufficient stock is available, return `true`; otherwise, return `false`.

}
