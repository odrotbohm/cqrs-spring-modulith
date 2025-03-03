package gae.piaz.modulith.cqrs.query.infrastructure;

import gae.piaz.modulith.cqrs.query.domain.ProductView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.QueryHint;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductViewRepository extends JpaRepository<ProductView, Long> {
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<ProductView> findByCategoryName(String categoryName);
    
    @Query("SELECT pv FROM ProductView pv WHERE pv.price BETWEEN :minPrice AND :maxPrice")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<ProductView> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                      @Param("maxPrice") BigDecimal maxPrice);
    
    // Custom projections for specific use cases
    interface ProductSummary {
        Long getId();
        String getName();
        BigDecimal getPrice();
        Integer getStock();
    }
    
    List<ProductSummary> findAllProjectedBy();
}