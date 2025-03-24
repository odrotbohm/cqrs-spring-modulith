package gae.piaz.modulith.cqrs.products.command;

import gae.piaz.modulith.cqrs.products.command.Product.ProductIdentifier;
import lombok.Getter;
import lombok.Setter;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;

@Getter
@Setter
class Review implements AggregateRoot<Review, ReviewIdentifier> {

	private ReviewIdentifier id;
	private Integer vote;
	private String comment;
	private Association<Product, ProductIdentifier> product;
}
