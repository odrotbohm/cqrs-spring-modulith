package gae.piaz.modulith.cqrs.products.command;

import gae.piaz.modulith.cqrs.products.DatabaseConfiguration;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;

import java.math.BigDecimal;

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
        assertThat(product.get().getPrice()).isEqualTo(BigDecimal.ONE);
        assertThat(product.get().getStock()).isEqualTo(100);
        assertThat(product.get().getCategory()).isEqualTo("Test Category");
        assertThat(product.get().getReviews()).isEmpty();
    }

    void testUpdateProduct() {

    }

    void testAddReview() {     }

}
