package gae.piaz.modulith.cqrs.command.api;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gae.piaz.modulith.cqrs.command.service.ProductCommandService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/products/commands")
@AllArgsConstructor
public class ProductCommandController {

    private final ProductCommandService commandService;

    // curl -X POST http://localhost:8080/api/products/commands \
    //   -H "Content-Type: application/json" \
    //   -d '{"name":"Product 1","description":"Description","price":99.99,"stock":100,"category":"Electronics"}'

    public record CreateProductRequest(String name, String description, BigDecimal price, Integer stock, String category) {

    }

    public record UpdateProductRequest(String name, String description, BigDecimal price, Integer stock, String category) {

    }

    public record AddReviewRequest(Integer vote, String comment) {

    }

    @PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody CreateProductRequest request) {
        Long id = commandService.createProduct(request.name(), request.description(), request.price(), request.stock(), request.category());
        return ResponseEntity.created(URI.create("/api/products/" + id))
            .body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest request) {
        commandService.updateProduct(id, request.name(), request.description(), request.price(), request.stock(), request.category());
        return ResponseEntity.noContent()
            .build();
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<Long> addReview(@PathVariable Long id, @RequestBody AddReviewRequest request) {
        Long reviewId = commandService.addReview(id, request.vote(), request.comment());
        return ResponseEntity.created(URI.create("/api/products/" + id + "/reviews/" + reviewId))
            .body(reviewId);
    }
}