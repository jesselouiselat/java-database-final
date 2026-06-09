package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Transactional
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequestDTO) {
        Customer customer = customerRepository.findByEmail(placeOrderRequestDTO.getCustomerEmail());
        if (customer == null) {
            customer= new Customer();
            customer.setEmail(placeOrderRequestDTO.getCustomerEmail());
            customer.setName(placeOrderRequestDTO.getCustomerName());
            customer.setPhone(placeOrderRequestDTO.getCustomerPhone());
            customer = customerRepository.save(customer);
        }

        Store store = storeRepository.findById(placeOrderRequestDTO.getStoreId())
                .orElseThrow(()->new RuntimeException("Error: Store Not Found with ID"
                        +placeOrderRequestDTO.getStoreId()));

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomer(customer);
        orderDetails.setStore(store);
        orderDetails.setTotalPrice(placeOrderRequestDTO.getTotalPrice());
        orderDetails.setDate(LocalDate.now());

        orderDetails = orderDetailsRepository.save(orderDetails);

        for (PurchaseProductDTO item : placeOrderRequestDTO.getPurchaseProduct())
        {
            Product product = productRepository.findById(item.getId())
                    .orElseThrow(()->new RuntimeException("Error: Product Not Found with ID" + item.getId()));



            Inventory inventory = inventoryRepository.findByProductIdAndStoreId(product.getId(), store.getId());

            if (inventory == null) {
                throw new RuntimeException("Error: Inventory Not Found with ID" + item.getId());
            }

            if ((inventory.getStockLevel() < item.getQuantity())){
                throw new RuntimeException("Error: Inventory Stock Not Enough For:" + item.getName());
            }

            inventory.setStockLevel(inventory.getStockLevel() - item.getQuantity());
            inventoryRepository.save(inventory);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderDetails(orderDetails);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItemRepository.save(orderItem);

        }



            }








// 1. **saveOrder Method**:
//    - Processes a customer's order, including saving the order details and associated items.
//    - Parameters: `PlaceOrderRequestDTO placeOrderRequest` (Request data for placing an order)
//    - Return Type: `void` (This method doesn't return anything, it just processes the order)

// 2. **Retrieve or Create the Customer**:
//    - Check if the customer exists by their email using `findByEmail`.
//    - If the customer exists, use the existing customer; otherwise, create and save a new customer using `customerRepository.save()`.

// 3. **Retrieve the Store**:
//    - Fetch the store by ID from `storeRepository`.
//    - If the store doesn't exist, throw an exception. Use `storeRepository.findById()`.

// 4. **Create OrderDetails**:
//    - Create a new `OrderDetails` object and set customer, store, total price, and the current timestamp.
//    - Set the order date using `java.time.LocalDateTime.now()` and save the order with `orderDetailsRepository.save()`.

// 5. **Create and Save OrderItems**:
//    - For each product purchased, find the corresponding inventory, update stock levels, and save the changes using `inventoryRepository.save()`.
//    - Create and save `OrderItem` for each product and associate it with the `OrderDetails` using `orderItemRepository.save()`.

   
}
