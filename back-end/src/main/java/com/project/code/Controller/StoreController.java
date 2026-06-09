package com.project.code.Controller;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;


    @PostMapping
    public ResponseEntity<Map<String, Object>> addStore(@RequestBody Store store){
        Map<String, Object> response = new HashMap<>();

        try{
            Store newStore = storeRepository.save(store);
            response.put("message", "New store added with ID:"+ newStore.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch(Exception e){
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("validate/{storeId}")
    public ResponseEntity<Boolean> validateStore(@PathVariable("storeId") Long storeId){
        try{
            boolean response;
            response = storeRepository.findById(storeId).isPresent();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }  catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO){
        Map<String, Object> response = new HashMap<>();
        try{
            orderService.saveOrder(placeOrderRequestDTO);
            response.put("message", "Order placed successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch(Exception e){
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to designate it as a REST controller for handling HTTP requests.
//    - Map the class to the `/store` URL using `@RequestMapping("/store")`.


 // 2. Autowired Dependencies:
//    - Inject the following dependencies via `@Autowired`:
//        - `StoreRepository` for managing store data.
//        - `OrderService` for handling order-related functionality.


 // 3. Define the `addStore` Method:
//    - Annotate with `@PostMapping` to create an endpoint for adding a new store.
//    - Accept `Store` object in the request body.
//    - Return a success message in a `Map<String, String>` with the key `message` containing store creation confirmation.


 // 4. Define the `validateStore` Method:
//    - Annotate with `@GetMapping("validate/{storeId}")` to check if a store exists by its `storeId`.
//    - Return a **boolean** indicating if the store exists.


 // 5. Define the `placeOrder` Method:
//    - Annotate with `@PostMapping("/placeOrder")` to handle order placement.
//    - Accept `PlaceOrderRequestDTO` in the request body.
//    - Return a success message with key `message` if the order is successfully placed.
//    - Return an error message with key `Error` if there is an issue processing the order.


   
}
