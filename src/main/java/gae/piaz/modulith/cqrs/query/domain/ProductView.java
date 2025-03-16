package gae.piaz.modulith.cqrs.query.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "product_views")
@Getter
@Setter
@NoArgsConstructor
public class ProductView {

    // Replicates Command fields
    @Id
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    
    // Specific to Query
    @Column(name = "average_rating")
    private Double averageRating = 0.0;
    @Column(name = "review_count")
    private Integer reviewCount = 0;
} 