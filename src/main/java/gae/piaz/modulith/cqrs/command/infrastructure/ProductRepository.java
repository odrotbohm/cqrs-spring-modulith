package gae.piaz.modulith.cqrs.command.infrastructure;

import gae.piaz.modulith.cqrs.command.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}