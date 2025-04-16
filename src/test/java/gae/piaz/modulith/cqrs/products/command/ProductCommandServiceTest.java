package gae.piaz.modulith.cqrs.products.command;

import static org.assertj.core.api.Assertions.*;

import gae.piaz.modulith.cqrs.products.DatabaseConfiguration;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;

@ApplicationModuleTest
@RequiredArgsConstructor
@Import(DatabaseConfiguration.class)
public class ProductCommandServiceTest {

	private final ProductCommandService productCommandService;
	private final ProductRepository productRepository;

	@Test
	void testAddProduct() {

		var productId = productCommandService.createProduct("Test Product", "Test Product Description", BigDecimal.ONE, 100,
				"Test Category");

		assertThat(productId.id()).isNotNull();

		assertThat(productRepository.findById(productId)).hasValueSatisfying(it -> {
			assertThat(it.getName()).isEqualTo("Test Product");
			assertThat(it.getDescription()).isEqualTo("Test Product Description");
			assertThat(it.getStock()).isEqualTo(100);
			assertThat(it.getCategory()).isEqualTo("Test Category");
			assertThat(it.getProductReviews()).isEmpty();
		});
	}

	@Test
	void testUpdateProduct() {

		var id = new Product.ProductIdentifier(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));

		productCommandService.updateProduct(id, "Laptop Pro 2", "High performance laptop for true PROs",
				new BigDecimal("1299.99"), 25, "Electronics");

		assertThat(productRepository.findById(id)).hasValueSatisfying(it -> {
			assertThat(it.getName()).isEqualTo("Laptop Pro 2");
			assertThat(it.getDescription()).isEqualTo("High performance laptop for true PROs");
		});
	}

	@Test
	void testAddReview() {

		var id = new Product.ProductIdentifier(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
		var review = productCommandService.addReview(id, 5, "Great product!");

		assertThat(productRepository.findById(id)).hasValueSatisfying(it -> {

			var assignedReview = it.getProductReviews().stream()
					.filter(review::equals)
					.findFirst();

			assertThat(assignedReview).hasValueSatisfying(foundReview -> {
				assertThat(foundReview.getVote()).isEqualTo(5);
				assertThat(foundReview.getComment()).isEqualTo("Great product!");
			});
		});
	}
}
