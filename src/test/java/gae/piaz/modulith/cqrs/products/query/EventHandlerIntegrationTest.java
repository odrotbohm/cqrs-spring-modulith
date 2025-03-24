package gae.piaz.modulith.cqrs.products.query;

import static org.assertj.core.api.Assertions.*;

import gae.piaz.modulith.cqrs.products.command.Product.ProductIdentifier;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductCreated;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductReviewed;
import gae.piaz.modulith.cqrs.products.command.ReviewIdentifier;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest
@RequiredArgsConstructor
class EventHandlerIntegrationTest {

	private final ProductViewRepository repository;

	@Test
	void whenReviewEventIsReceived_thenUpdateProductView(Scenario scenario) {

		var id = new ProductIdentifier(UUID.randomUUID());

		scenario.publish(new ProductCreated(id, "test", "test", BigDecimal.TEN, 100, "test"))
				.andWaitForStateChange(() -> repository.findById(id));

		scenario.publish(new ProductReviewed(id, new ReviewIdentifier(UUID.randomUUID()), 5, "test"))
				.andWaitForStateChange(() -> repository.findById(id))
				.andVerify(view -> {
					assertThat(view).hasValueSatisfying(it -> {
						assertThat(it.getReviewCount()).isEqualTo(1);
						assertThat(it.getAverageRating()).isEqualTo(5);
					});
				});
	}
}
