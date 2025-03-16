package gae.piaz.modulith.cqrs.query.service;

import gae.piaz.modulith.cqrs.command.events.ProductCreatedEvent;
import gae.piaz.modulith.cqrs.command.events.ProductReviewEvent;
import gae.piaz.modulith.cqrs.command.events.ProductUpdatedEvent;
import gae.piaz.modulith.cqrs.query.domain.ProductView;
import gae.piaz.modulith.cqrs.query.domain.ProductViewRepository;
import lombok.AllArgsConstructor;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductEventHandler {
    
    private final ProductViewRepository viewRepository;

    @ApplicationModuleListener
    public void on(ProductCreatedEvent event) {
        ProductView view = new ProductView();
        view.setId(event.id());
        view.setName(event.name());
        view.setDescription(event.description());
        view.setPrice(event.price());
        view.setStock(event.stock());
        view.setCategory(event.category());
        viewRepository.save(view);
    }
    
    @ApplicationModuleListener
    public void on(ProductUpdatedEvent event) {
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
    public void on(ProductReviewEvent event) {
        viewRepository.findById(event.productId()).ifPresent(view -> {

            double currentTotal = view.getAverageRating() * view.getReviewCount();
            int newCount = view.getReviewCount() + 1;
            double newAverage = (currentTotal + event.vote()) / newCount;
            
            view.setReviewCount(newCount);
            view.setAverageRating(newAverage);
            
            viewRepository.save(view);
        });
    }
} 