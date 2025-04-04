package gae.piaz.modulith.cqrs.products.command;

import gae.piaz.modulith.cqrs.products.command.Product.ProductIdentifier;
import org.springframework.data.repository.CrudRepository;

interface ProductRepository extends CrudRepository<Product, ProductIdentifier> {}
