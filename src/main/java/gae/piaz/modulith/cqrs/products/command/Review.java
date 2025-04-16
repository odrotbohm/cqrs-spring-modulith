package gae.piaz.modulith.cqrs.products.command;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import org.jmolecules.ddd.types.Entity;

@Getter
class Review implements Entity<Product, ReviewIdentifier> {

	private final ReviewIdentifier id;
	private @Setter Integer vote;
	private @Setter String comment;

	public Review() {
		this.id = new ReviewIdentifier(UUID.randomUUID());
	}
}
