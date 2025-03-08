package gae.piaz.modulith.cqrs;

import gae.piaz.modulith.cqrs.command.service.ProductCommandService;
import gae.piaz.modulith.cqrs.command.domain.Product;
import gae.piaz.modulith.cqrs.command.domain.ProductRepository;
import gae.piaz.modulith.cqrs.query.domain.ProductView;
import gae.piaz.modulith.cqrs.query.domain.ProductViewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static java.time.Duration.ofSeconds;

@SpringBootTest
class ProductCatalogIntegrationTest {

    @Autowired
    private ProductCommandService commandService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductViewRepository viewRepository;

    @Test
    void shouldCreateProductAndGenerateView() {
        // Given
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = BigDecimal.valueOf(19.99);
        Integer stock = 100;
        String category = "Test Category";

        // When
        Long productId = commandService.createProduct(name, description, price, stock, category);

        // Then
        // Command side check
        Optional<Product> product = productRepository.findById(productId);
        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo(name);
        assertThat(product.get().getPrice()).isEqualTo(price);

        // Query side check - might need to wait for event processing
        await().atMost(ofSeconds(5)).untilAsserted(() -> {
            Optional<ProductView> view = viewRepository.findById(productId);
            assertThat(view).isPresent();
            assertThat(view.get().getName()).isEqualTo(name);
            assertThat(view.get().getPrice()).isEqualTo(price);
            assertThat(view.get().getCategoryName()).isEqualTo(category);
        });
    }

    @Test
    void shouldUpdateProductStockAndReflectInView() {
        // Given
        Long productId = commandService.createProduct(
            "Stock Test", "Description", BigDecimal.TEN, 50, "Category");
        Integer stockChange = -5;

        // When
        commandService.updateStock(productId, stockChange);

        // Then
        // Command side check
        Product product = productRepository.findById(productId).orElseThrow();
        assertThat(product.getStock()).isEqualTo(45);

        // Query side check
        await().atMost(ofSeconds(5)).untilAsserted(() -> {
            ProductView view = viewRepository.findById(productId).orElseThrow();
            assertThat(view.getStock()).isEqualTo(45);
        });
    }

    @Test
    void shouldFindProductsByCategory() {
        // Given
        String category = "Electronics";
        commandService.createProduct("Phone", "Smart Phone", BigDecimal.valueOf(999.99), 10, category);
        commandService.createProduct("Laptop", "Powerful Laptop", BigDecimal.valueOf(1999.99), 5, category);

        // When (wait for views to be created)
        await().atMost(ofSeconds(5)).until(() -> viewRepository.findByCategoryName(category).size() >= 2);

        // Then
        List<ProductView> products = viewRepository.findByCategoryName(category);
        assertThat(products).hasSize(2);
        assertThat(products).extracting(ProductView::getCategoryName).containsOnly(category);
    }
} 