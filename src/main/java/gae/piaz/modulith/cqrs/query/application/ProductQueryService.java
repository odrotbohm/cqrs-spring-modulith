package gae.piaz.modulith.cqrs.query.application;

import gae.piaz.modulith.cqrs.query.domain.ProductView;
import gae.piaz.modulith.cqrs.query.domain.ProductViewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductQueryService {
    private final ProductViewRepository viewRepository;
    
    public ProductQueryService(ProductViewRepository viewRepository) {
        this.viewRepository = viewRepository;
    }
    
    public List<ProductView> findAllProducts() {
        return viewRepository.findAll();
    }
    
    public ProductView findById(Long id) {
        return viewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product view not found with id: " + id));
    }
    
    public List<ProductView> findByCategory(String category) {
        return viewRepository.findByCategory(category);
    }
    
    public List<ProductView> findByPriceRange(BigDecimal min, BigDecimal max) {
        return viewRepository.findByPriceRange(min, max);
    }
} 