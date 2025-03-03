package gae.piaz.modulith.cqrs.catalog.command.api;

import gae.piaz.modulith.cqrs.catalog.command.application.ProductCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/products/commands")
public class ProductCommandController {
    private final ProductCommandService commandService;
    
    public ProductCommandController(ProductCommandService commandService) {
        this.commandService = commandService;
    }
    
    @PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody CreateProductRequest request) {
        Long id = commandService.createProduct(
            request.name(), 
            request.description(),
            request.price(),
            request.stock(),
            request.category()
        );
        return ResponseEntity.created(URI.create("/api/products/" + id)).body(id);
    }
    
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Long id, 
                                           @RequestBody UpdateStockRequest request) {
        commandService.updateStock(id, request.quantityChange());
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id,
                                             @RequestBody UpdateProductRequest request) {
        commandService.updateProduct(
            id,
            request.name(),
            request.description(),
            request.price(),
            request.category()
        );
        return ResponseEntity.noContent().build();
    }
    
    // Request records
    public record CreateProductRequest(String name, String description, BigDecimal price, Integer stock, String category) {}
    public record UpdateStockRequest(Integer quantityChange) {}
    public record UpdateProductRequest(String name, String description, BigDecimal price, String category) {}
} 