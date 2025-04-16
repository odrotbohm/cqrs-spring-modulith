package gae.piaz.modulith.cqrs.products.command;

import gae.piaz.modulith.cqrs.products.command.Product.ProductIdentifier;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
class ProductCommandController {

	private final ProductCommandService commandService;

	// curl -X POST http://localhost:8080/api/products/commands \
	// -H "Content-Type: application/json" \
	// -d '{"name":"Product 1","description":"Description","price":99.99,"stock":100,"category":"Electronics"}'

	@PostMapping
	ResponseEntity<ProductIdentifier> createProduct(@RequestBody CreateProductRequest request) {

		var id = commandService.createProduct(request.name(), request.description(), request.price(), request.stock(),
				request.category());

		return ResponseEntity.created(URI.create("/api/products/" + id.id())).body(id);
	}

	@PutMapping("/{id}")
	ResponseEntity<Void> updateProduct(@PathVariable ProductIdentifier id,
			@RequestBody UpdateProductRequest request) {

		commandService.updateProduct(id, request.name(), request.description(), request.price(), request.stock(),
				request.category());

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/reviews")
	ResponseEntity<ReviewIdentifier> addReview(@PathVariable ProductIdentifier productIdentifier,
			@RequestBody AddReviewRequest request) {

		var review = commandService.addReview(productIdentifier, request.vote(), request.comment());
		var reviewId = review.getId();

		return ResponseEntity
				.created(URI.create("/api/products/" + productIdentifier.id() + "/reviews/" + reviewId.id()))
				.body(reviewId);
	}

	record CreateProductRequest(String name, String description, BigDecimal price, Integer stock,
			String category) {}

	record UpdateProductRequest(String name, String description, BigDecimal price, Integer stock,
			String category) {}

	record AddReviewRequest(Integer vote, String comment) {}
}
