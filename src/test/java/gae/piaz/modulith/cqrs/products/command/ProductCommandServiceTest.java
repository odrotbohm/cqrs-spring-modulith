package gae.piaz.modulith.cqrs.products.command;

import gae.piaz.modulith.cqrs.products.DatabaseConfiguration;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
@RequiredArgsConstructor
@Import(DatabaseConfiguration.class)
public class ProductCommandServiceTest {

    private final ProductCommandService productCommandService;

    private final ProductRepository productRepository;

    @Test
    void testAddProduct() {
        Product.ProductIdentifier productId = productCommandService.createProduct("Test Product",
                "Test Product Description",
                BigDecimal.ONE,
                100,
                "Test Category");

        assertThat(productId.id()).isNotNull();
        var product = productRepository.findById(productId);
        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("Test Product");
        assertThat(product.get().getDescription()).isEqualTo("Test Product Description");
        assertThat(product.get().getStock()).isEqualTo(100);
        assertThat(product.get().getCategory()).isEqualTo("Test Category");
        assertThat(product.get().getReviews()).isEmpty();
    }

    @Test
    void testUpdateProduct() {
        UUID productId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        productCommandService.updateProduct(new Product.ProductIdentifier(productId),
                "Laptop Pro 2",
                "High performance laptop for true PROs",
                new BigDecimal("1299.99"),
                25,
                "Electronics"
        );
        var foundProduct = productRepository.findById(new Product.ProductIdentifier(productId));
        assertThat(foundProduct.isPresent()).isTrue();
        var updatedProduct = foundProduct.get();
        assertThat(updatedProduct.getName()).isEqualTo("Laptop Pro 2");
        assertThat(updatedProduct.getDescription()).isEqualTo("High performance laptop for true PROs");
    }

    @Test
    void testAddReview() {
        UUID productId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        UUID reviewId = productCommandService.addReview(new Product.ProductIdentifier(productId), 5, "Great product!").id();
        var foundProduct = productRepository.findById(new Product.ProductIdentifier(productId));
        assertThat(foundProduct.isPresent()).isTrue();
        Optional<Review> foundReview = foundProduct.get().getReviews().stream().filter(it -> it.getId().id().equals(reviewId)).findFirst();
        assertThat(foundReview.isPresent()).isTrue();
        assertThat(foundReview.get().getVote()).isEqualTo(5);
        assertThat(foundReview.get().getComment()).isEqualTo("Great product!");
        assertThat(foundReview.get().getProduct().getId().id()).isEqualTo(productId);
    }

}
