package gae.piaz.modulith.cqrs.catalog.command.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public Product(String name, String description, BigDecimal price, Integer stock, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }
    
    public void updateStock(Integer quantityChange) {
        // Apply business logic
        if (this.stock + quantityChange < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        
        this.stock += quantityChange;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updatePrice(BigDecimal newPrice) {
        // Business rule: prices can only increase by up to 20%
        if (newPrice.compareTo(this.price.multiply(BigDecimal.valueOf(1.2))) > 0) {
            throw new IllegalArgumentException("Price increase exceeds maximum allowed");
        }
        
        this.price = newPrice;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void update(String name, String description, BigDecimal price, String category) {
        this.name = name;
        this.description = description;
        
        // Reuse price validation logic
        if (price != null && !price.equals(this.price)) {
            updatePrice(price);
        }
        
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }
} 