package gae.piaz.modulith.cqrs.query;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;

import gae.piaz.modulith.cqrs.command.events.ProductCreatedEvent;
import gae.piaz.modulith.cqrs.command.events.ProductReviewEvent;
import gae.piaz.modulith.cqrs.query.domain.ProductView;
import gae.piaz.modulith.cqrs.query.domain.ProductViewRepository;
import gae.piaz.modulith.cqrs.query.service.ProductEventHandler;

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES, extraIncludes = "shared")
class EventHandlerIntegrationTest {

    @Autowired
    private ProductViewRepository viewRepository;

    @Autowired
    private ProductEventHandler eventHandler;

    @Test
    void whenReviewEventIsReceived_thenUpdateProductView() {
        // Given
        ProductCreatedEvent createdEvent = new ProductCreatedEvent(1L, "test", "test", BigDecimal.TEN, 100, "test");
        ProductReviewEvent reviewEvent = new ProductReviewEvent(1L, 1L, 5, "test");

        // When
        eventHandler.on(createdEvent);
        await().atMost(ofSeconds(5))
            .untilAsserted(() -> {
                ProductView view = viewRepository.findById(1L)
                    .get();
                assertThat(view).isNotNull();
            });

        eventHandler.on(reviewEvent);
        await().atMost(ofSeconds(5))
            .untilAsserted(() -> {
                ProductView view = viewRepository.findById(1L)
                    .get();
                assertThat(view.getReviewCount()).isEqualTo(1);
                assertThat(view.getAverageRating()).isEqualTo(5);
            });
    }
} 