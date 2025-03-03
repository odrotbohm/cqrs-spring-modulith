package gae.piaz.modulith.cqrs.query.api;

import gae.piaz.modulith.cqrs.query.application.ProductQueryService;
import gae.piaz.modulith.cqrs.query.domain.ProductView;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products/queries")
public class ProductQueryController {
    private final ProductQueryService queryService;
    
    public ProductQueryController(ProductQueryService queryService) {
        this.queryService = queryService;
    }
    
    @GetMapping
    public List<ProductView> getAllProducts() {
        return queryService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ProductView getProductById(@PathVariable Long id) {
        return queryService.findById(id);
    }
    
    @GetMapping("/by-category")
    public List<ProductView> getProductsByCategory(@RequestParam String category) {
        return queryService.findByCategory(category);
    }
    
    @GetMapping("/by-price")
    public List<ProductView> getProductsByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return queryService.findByPriceRange(min, max);
    }
} 