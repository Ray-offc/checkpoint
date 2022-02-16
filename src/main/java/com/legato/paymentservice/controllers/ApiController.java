package com.legato.paymentservice.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.legato.paymentservice.beans.Customer;
import com.legato.paymentservice.beans.Item;
import com.legato.paymentservice.beans.Product;
import com.legato.paymentservice.service.CustomerService;
import com.legato.paymentservice.service.ItemService;
import com.legato.paymentservice.service.ProductService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ItemService itemService;

	@Autowired
	private ProductService productService;
	
	@GetMapping("/greet")
	public ResponseEntity<Object> greetings() {
		return ResponseEntity.status(200).body("Hello Microservices World!");
	}
	


	@GetMapping(path = "/fetchAll")
	public ResponseEntity<Object> fetchAllItems() {
		List<Item> itemList = itemService.fetchAllItems();
		return ResponseEntity.status(HttpStatus.OK).body(itemList);
	}

	@GetMapping(path = "/fetchById/{itemId}")
	public ResponseEntity<Object> fecthProductDetails(@PathVariable int itemId) {

		List<Product> productList = productService.fetchProducts(itemId);

		List<Product> sortedList = productList.stream().filter(curobj -> curobj.getItemId() == itemId)
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(sortedList);
	}
	
	@GetMapping(path = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchAll() {

		ResponseEntity<Object> response = null;
		List<Customer> listUsers = customerService.getAll();
		response = ResponseEntity.status(HttpStatus.OK).body(listUsers);
		return response;
	}

	@PostMapping(path = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveUser(@RequestBody Customer customer) {

		ResponseEntity<Object> response = null;
		// System.out.println(customer.getA);
		if (customer != null) {
			Customer createdCustomer = customerService.saveCustomer(customer);
			response = ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
		} else {
			response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
		}
		return response;
	}

	@GetMapping(path = "/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> findUser(@PathVariable("id") String id) {
		ResponseEntity<Object> response = null;
		try {
			Customer u = customerService.getByOne(Long.parseLong(id));
			response = ResponseEntity.status(HttpStatus.OK).body(u);
		} catch (Exception e) {
			// msg = new CustomMessage(e.getMessage(), 400);
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
		}
		return response;
	}
	
}
