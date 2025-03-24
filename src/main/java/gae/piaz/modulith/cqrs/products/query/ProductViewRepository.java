package gae.piaz.modulith.cqrs.products.query;

import gae.piaz.modulith.cqrs.products.command.Product.ProductIdentifier;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

interface ProductViewRepository extends ListCrudRepository<ProductView, ProductIdentifier> {

	List<ProductView> findByCategory(String categoryName);

	@Query("SELECT pv FROM ProductView pv WHERE pv.price BETWEEN :minPrice AND :maxPrice")
	List<ProductView> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

	@Query("SELECT pv FROM ProductView pv WHERE pv.reviewCount > 0 ORDER BY pv.averageRating DESC")
	List<ProductView> findAllOrderByRatingDesc();
}
