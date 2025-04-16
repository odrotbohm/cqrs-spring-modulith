package gae.piaz.modulith.cqrs.products.command;

import gae.piaz.modulith.cqrs.products.command.Product.ProductIdentifier;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductCreated;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductReviewed;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductUpdated;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class ProductCommandService {

	private final ProductRepository products;
	private final ApplicationEventPublisher eventPublisher;

	ProductIdentifier createProduct(String name, String description, BigDecimal price, Integer stock,
			String category) {

		var product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setStock(stock);
		product.setCategory(category);

		var saved = products.save(product);

		eventPublisher.publishEvent(
				new ProductCreated(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice(),
						saved.getStock(), saved.getCategory()));

		return saved.getId();
	}

	void updateProduct(ProductIdentifier productId, String name, String description, BigDecimal price, Integer stock,
			String category) {

		var product = products.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setStock(stock);
		product.setCategory(category);

		products.save(product);

		eventPublisher.publishEvent(
				new ProductUpdated(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
						product.getStock(),
						product.getCategory()));
	}

	Review addReview(ProductIdentifier productId, Integer vote, String comment) {

		if (vote < 0 || vote > 5) {
			throw new IllegalArgumentException("Vote must be between 0 and 5");
		}

		var review = new Review();
		review.setVote(vote);
		review.setComment(comment);

		var product = products.findById(productId)
				.map(it -> it.add(review))
				.map(products::save)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

		eventPublisher
				.publishEvent(new ProductReviewed(product.getId(), review.getId(), review.getVote(), review.getComment()));

		return review;
	}
}
