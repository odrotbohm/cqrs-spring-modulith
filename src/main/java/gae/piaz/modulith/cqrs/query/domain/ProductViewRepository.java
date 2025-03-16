package gae.piaz.modulith.cqrs.query.domain;

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

    List<ProductView> findByCategory(String categoryName);
    
    @Query("SELECT pv FROM ProductView pv WHERE pv.price BETWEEN :minPrice AND :maxPrice")
    List<ProductView> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                      @Param("maxPrice") BigDecimal maxPrice);

}