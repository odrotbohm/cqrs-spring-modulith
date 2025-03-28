package gae.piaz.modulith.cqrs.products.query;

import gae.piaz.modulith.cqrs.products.command.Product.ProductIdentifier;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Transactional(readOnly = true, transactionManager = "queryTransactionManager")
class ProductViewController {

	private final ProductViewRepository repository;

	@GetMapping
	List<ProductView> getAllProducts() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	ResponseEntity<ProductView> getProductById(@PathVariable ProductIdentifier id) {
		return ResponseEntity.of(repository.findById(id));
	}

	@GetMapping("/by-category")
	List<ProductView> getProductsByCategory(@RequestParam String category) {
		return repository.findByCategory(category);
	}

	@GetMapping("/by-price")
	List<ProductView> getProductsByPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
		return repository.findByPriceRange(min, max);
	}

	@GetMapping("/by-rating")
	List<ProductView> getProductsByRating() {
		return repository.findAllOrderByRatingDesc();
	}
}
