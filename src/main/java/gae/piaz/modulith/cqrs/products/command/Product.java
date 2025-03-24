package gae.piaz.modulith.cqrs.products.command;

import gae.piaz.modulith.cqrs.products.command.Product.ProductIdentifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Product implements AggregateRoot<Product, ProductIdentifier> {

	private ProductIdentifier id;
	private String name, description, category;
	private BigDecimal price;
	private Integer stock;

	private List<Review> reviews = new ArrayList<>();

	public record ProductIdentifier(UUID id) implements Identifier {}
}
