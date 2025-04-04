package gae.piaz.modulith.cqrs.products.command;

import gae.piaz.modulith.cqrs.products.command.Product.ProductIdentifier;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductCreated;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductReviewed;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductUpdated;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import org.jmolecules.ddd.types.Association;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class ProductCommandService {

	private final ProductRepository productRepository;
	private final ReviewRepository reviewRepository;
	private final ApplicationEventPublisher eventPublisher;

	ProductIdentifier createProduct(String name, String description, BigDecimal price, Integer stock,
			String category) {

		var product = new Product();
		product.setId(new ProductIdentifier(UUID.randomUUID()));
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setStock(stock);
		product.setCategory(category);

		var saved = productRepository.save(product);

		eventPublisher.publishEvent(
				new ProductCreated(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice(),
						saved.getStock(), saved.getCategory()));
		return saved.getId();
	}

	void updateProduct(ProductIdentifier productId, String name, String description, BigDecimal price, Integer stock,
			String category) {

		var product = productRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setStock(stock);
		product.setCategory(category);

		productRepository.save(product);

		eventPublisher.publishEvent(
				new ProductUpdated(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
						product.getStock(),
						product.getCategory()));
	}

	ReviewIdentifier addReview(ProductIdentifier productId, Integer vote, String comment) {

		if (vote < 0 || vote > 5) {
			throw new IllegalArgumentException("Vote must be between 0 and 5");
		}

		var product = productRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

		var review = new Review();
		review.setVote(vote);
		review.setComment(comment);
		review.setProduct(Association.forAggregate(product));

		var saved = reviewRepository.save(review);

		eventPublisher
				.publishEvent(new ProductReviewed(product.getId(), saved.getId(), saved.getVote(), saved.getComment()));

		return saved.getId();
	}
}
