package com.project.code.Controller;


import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @GetMapping("/{storeId}/{productId}")
    public ResponseEntity<Map<String, Object>> getAllReviews(@PathVariable Long storeId, @PathVariable Long productId) {
        Map<String, Object> response = new HashMap<>();

        try{
            List<Review> reviews = reviewRepository.findByStoreIdAndProductId(storeId, productId);
            List<Map<String, Object>> formattedList = new ArrayList<>();

            for (Review review : reviews) {
                Map<String, Object> simplifiedReview = new HashMap<>();
                simplifiedReview.put("comment", review.getComment());
                simplifiedReview.put("rating", review.getRating());

                Optional<Customer> customerOpt = customerRepository.findById(review.getCustomerId());
                if (customerOpt.isPresent()) {
                    simplifiedReview.put("customerName", customerOpt.get().getName());
                } else  {
                    simplifiedReview.put("customerName", "Unknown");
                }
                formattedList.add(simplifiedReview);
            }
            response.put("reviews", formattedList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to designate it as a REST controller for handling HTTP requests.
//    - Map the class to the `/reviews` URL using `@RequestMapping("/reviews")`.


 // 2. Autowired Dependencies:
//    - Inject the following dependencies via `@Autowired`:
//        - `ReviewRepository` for accessing review data.
//        - `CustomerRepository` for retrieving customer details associated with reviews.


// 3. Define the `getReviews` Method:
//    - Annotate with `@GetMapping("/{storeId}/{productId}")` to fetch reviews for a specific product in a store by `storeId` and `productId`.
//    - Accept `storeId` and `productId` via `@PathVariable`.
//    - Fetch reviews using `findByStoreIdAndProductId()` method from `ReviewRepository`.
//    - Filter reviews to include only `comment`, `rating`, and the `customerName` associated with the review.
//    - Use `findById(review.getCustomerId())` from `CustomerRepository` to get customer name.
//    - Return filtered reviews in a `Map<String, Object>` with key `reviews`.

    
   
}
