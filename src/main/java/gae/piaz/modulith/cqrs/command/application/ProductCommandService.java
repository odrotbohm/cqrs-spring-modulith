package gae.piaz.modulith.cqrs.command.application;

import gae.piaz.modulith.cqrs.command.domain.Product;
import gae.piaz.modulith.cqrs.command.events.ProductCreatedEvent;
import gae.piaz.modulith.cqrs.command.events.ProductStockChangedEvent;
import gae.piaz.modulith.cqrs.command.events.ProductUpdatedEvent;
import gae.piaz.modulith.cqrs.command.infrastructure.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    public ProductCommandService(ProductRepository productRepository, ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }
    
    public Long createProduct(String name, String description, BigDecimal price, Integer stock, String category) {
        Product product = new Product(name, description, price, stock, category);
        Product saved = productRepository.save(product);
        
        // Publish event
        eventPublisher.publishEvent(new ProductCreatedEvent(
            saved.getId(),
            saved.getName(),
            saved.getDescription(),
            saved.getPrice(),
            saved.getStock(),
            saved.getCategory(),
            LocalDateTime.now()
        ));
        
        return saved.getId();
    }
    
    public void updateStock(Long productId, Integer quantityChange) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
            
        product.updateStock(quantityChange);
        productRepository.save(product);
        
        // Publish event
        eventPublisher.publishEvent(new ProductStockChangedEvent(
            product.getId(),
            product.getStock(),
            quantityChange,
            LocalDateTime.now()
        ));
    }
    
    public void updateProduct(Long productId, String name, String description, BigDecimal price, String category) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
            
        product.update(name, description, price, category);
        productRepository.save(product);
        
        // Publish event
        eventPublisher.publishEvent(new ProductUpdatedEvent(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getCategory(),
            LocalDateTime.now()
        ));
    }
} 