package gae.piaz.modulith.cqrs.command.service;

import gae.piaz.modulith.cqrs.command.domain.Product;
import gae.piaz.modulith.cqrs.command.events.ProductCreatedEvent;
import gae.piaz.modulith.cqrs.command.events.ProductUpdatedEvent;
import gae.piaz.modulith.cqrs.command.domain.ProductRepository;
import lombok.AllArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Long createProduct(String name, String description, BigDecimal price, Integer stock, String category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);

        Product saved = productRepository.save(product);

        eventPublisher.publishEvent(new ProductCreatedEvent(
            saved.getId(),
            saved.getName(),
            saved.getDescription(),
            saved.getPrice(),
            saved.getStock(),
            saved.getCategory()
        ));
        return saved.getId();
    }
    
    public void updateProduct(Long productId, String name, String description, BigDecimal price, Integer stock, String category) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        productRepository.save(product);
        
        eventPublisher.publishEvent(new ProductUpdatedEvent(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getCategory()
        ));
    }
} 