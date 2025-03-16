package gae.piaz.modulith.cqrs.command;

import java.math.BigDecimal;
import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;

import gae.piaz.modulith.cqrs.command.domain.Product;
import gae.piaz.modulith.cqrs.command.domain.ProductRepository;
import gae.piaz.modulith.cqrs.command.events.ProductReviewEvent;
import gae.piaz.modulith.cqrs.command.service.ProductCommandService;

@ApplicationModuleTest
class CommandModuleIntegrationTest {

    @Autowired
    private ProductCommandService commandService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenAddingReview_eventIsSent(PublishedEvents events) {

        // Given
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(19.99));
        product.setStock(100);
        product.setCategory("Test Category");
        product = productRepository.save(product);

        // When
        Long productId = commandService.addReview(product.getId(), 5, "Test Review");

        // Then
        PublishedEvents.TypedPublishedEvents<ProductReviewEvent> matchingMapped = events.ofType(ProductReviewEvent.class);
        Iterator<ProductReviewEvent> eventIterator = matchingMapped.iterator();
        ProductReviewEvent event = eventIterator.next();
        Assertions.assertEquals(productId, event.productId());
        Assertions.assertEquals(5, event.vote());
        Assertions.assertEquals("Test Review", event.comment());
    }

}