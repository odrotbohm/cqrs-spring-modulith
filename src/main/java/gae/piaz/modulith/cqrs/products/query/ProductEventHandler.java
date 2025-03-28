package gae.piaz.modulith.cqrs.products.query;

import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductCreated;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductReviewed;
import gae.piaz.modulith.cqrs.products.command.ProductEvents.ProductUpdated;
import lombok.RequiredArgsConstructor;

import org.jmolecules.event.annotation.DomainEventHandler;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "queryTransactionManager")
class ProductEventHandler {

	private final ProductViewRepository viewRepository;

	@DomainEventHandler
	@ApplicationModuleListener
	void on(ProductCreated event) {

		var view = new ProductView();
		view.setId(event.id());
		view.setName(event.name());
		view.setDescription(event.description());
		view.setPrice(event.price());
		view.setStock(event.stock());
		view.setCategory(event.category());

		viewRepository.save(view);
	}

	@DomainEventHandler
	@ApplicationModuleListener
	void on(ProductUpdated event) {

		viewRepository.findById(event.id()).ifPresent(view -> {

			view.setName(event.name());
			view.setDescription(event.description());
			view.setPrice(event.price());
			view.setStock(event.stock());
			view.setCategory(event.category());
			viewRepository.save(view);
		});
	}

	@ApplicationModuleListener
	void on(ProductReviewed event) {

		viewRepository.findById(event.productId())
				.map(it -> it.on(event))
				.ifPresent(viewRepository::save);
	}
}
