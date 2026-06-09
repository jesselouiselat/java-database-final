package com.project.code.Repo;

import com.project.code.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Inventory findByProductIdAndStoreId(Long productId, Long storeId);
    List<Inventory> findByStoreId(Long storeId);

    void deleteByProductId(Long productId);



// 1. Add the repository interface:
//    - Extend JpaRepository<Inventory, Long> to inherit basic CRUD functionality.
//    - This allows the repository to perform operations like save, delete, update, and find without having to implement these methods manually.

// Example: public interface InventoryRepository extends JpaRepository<Inventory, Long> {}

// 2. Add custom query methods:
//    - **findByProductIdandStoreId**:
//      - This method will allow you to find an inventory record by its product ID and store ID.
//      - Return type: Inventory
//      - Parameters: Long productId, Long storeId
      
// Example: public Inventory findByProductIdandStoreId(Long productId, Long storeId);

//    - **findByStore_Id**:
//      - This method will allow you to find a list of inventory records for a specific store.
//      - Return type: List<Inventory>
//      - Parameter: Long storeId
      
// Example: public List<Inventory> findByStore_Id(Long storeId);

//    - **deleteByProductId**:
//      - This method will allow you to delete all inventory records related to a specific product ID.
//      - Return type: void
//      - Parameter: Long productId
//      - Use @Modifying and @Transactional annotations to ensure the database is modified correctly.



}
