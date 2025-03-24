package gae.piaz.modulith.cqrs.products.query;

import gae.piaz.modulith.cqrs.products.command.Product.ProductIdentifier;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductReviewed;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.ddd.types.AggregateRoot;

@Getter
@Setter
@QueryModel
class ProductView implements AggregateRoot<ProductView, ProductIdentifier> {

	private ProductIdentifier id;
	private String name, description, category;
	private BigDecimal price;
	private Integer stock;

	private @Setter(AccessLevel.NONE) Double averageRating = 0.0;
	private @Setter(AccessLevel.NONE) Integer reviewCount = 0;

	public ProductView on(ProductReviewed event) {

		double currentTotal = averageRating * reviewCount;

		this.reviewCount = reviewCount + 1;
		this.averageRating = (currentTotal + event.vote()) / reviewCount;

		return this;
	}
}
