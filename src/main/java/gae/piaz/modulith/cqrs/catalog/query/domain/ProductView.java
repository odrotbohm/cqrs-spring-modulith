package gae.piaz.modulith.cqrs.catalog.query.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_views")
@Getter
@Setter
@NoArgsConstructor
public class ProductView {
    @Id
    private Long id;
    
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    
    @Column(name = "category_name")
    private String categoryName;
    
    @Column(name = "average_rating")
    private Double averageRating = 0.0;
    
    @Column(name = "review_count")
    private Integer reviewCount = 0;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();
} 