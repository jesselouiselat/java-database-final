package com.project.code.Service;


import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceClass {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository  productRepository;

    public boolean validateInventory(Inventory inventory){
        Optional<Inventory> existingInventory = Optional.ofNullable(inventoryRepository.findByProductIdAndStoreId(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        ));
        return existingInventory.isEmpty();
    }
    public boolean validateProduct(Product product){
        Optional<Product> existingProduct =productRepository.findByName(product.getName());
        return existingProduct.isEmpty();
    }

    public boolean validateProductId(long id){
        Product product1 =  productRepository.findById(id);
        return product1 != null;
    }

    public Inventory getInventoryId(Inventory inventory){
        Optional<Inventory> inventory1 = Optional.ofNullable(inventoryRepository.findByProductIdAndStoreId(inventory.getProduct().getId(), inventory.getStore().getId()));
        return inventory1.orElse(null);
    }



// 1. **validateInventory Method**:
//    - Checks if an inventory record exists for a given product and store combination.
//    - Parameters: `Inventory inventory`
//    - Return Type: `boolean` (Returns `false` if inventory exists, otherwise `true`)

// 2. **validateProduct Method**:
//    - Checks if a product exists by its name.
//    - Parameters: `Product product`
//    - Return Type: `boolean` (Returns `false` if a product with the same name exists, otherwise `true`)

// 3. **ValidateProductId Method**:
//    - Checks if a product exists by its ID.
//    - Parameters: `long id`
//    - Return Type: `boolean` (Returns `false` if the product does not exist with the given ID, otherwise `true`)

// 4. **getInventoryId Method**:
//    - Fetches the inventory record for a given product and store combination.
//    - Parameters: `Inventory inventory`
//    - Return Type: `Inventory` (Returns the inventory record for the product-store combination)

}
