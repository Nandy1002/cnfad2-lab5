package com.cnfad2.lab5;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/products")
public class ProductController {
	private final List<Product> products = new ArrayList<Product>();
	
	@PostMapping
	public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, BindingResult result){
		if(result.hasErrors()) {
			List<String> errors = new ArrayList<String>();
			result.getFieldErrors().forEach(error -> errors.add(error.getField()+":"+error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(errors);
		}
		product.setId((long)products.size()+1);
		products.add(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(products);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product, BindingResult result) {
		if(result.hasErrors()) {
			List<String> errors = new ArrayList<String>();
			result.getFieldErrors().forEach(error -> errors.add(error.getField()+" : "+error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(errors);
		}
		
		Product oldProduct = products.stream().filter(pd->pd.getId().equals(id))
													.findFirst().orElse(null);
		
		if(oldProduct!=null) {
			oldProduct.setName(product.getName());
			oldProduct.setPrice(product.getPrice());
			return ResponseEntity.ok(oldProduct);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found");
		}
	}

	@GetMapping
	public List<Product> getMethodName() {
		return products;
	}
	
}
