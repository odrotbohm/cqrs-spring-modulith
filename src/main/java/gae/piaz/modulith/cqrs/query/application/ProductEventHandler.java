package gae.piaz.modulith.cqrs.query.application;

import gae.piaz.modulith.cqrs.command.events.ProductCreatedEvent;
import gae.piaz.modulith.cqrs.command.events.ProductStockChangedEvent;
import gae.piaz.modulith.cqrs.command.events.ProductUpdatedEvent;
import gae.piaz.modulith.cqrs.query.domain.ProductView;
import gae.piaz.modulith.cqrs.query.infrastructure.ProductViewRepository;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductEventHandler {
    private final ProductViewRepository viewRepository;
    
    public ProductEventHandler(ProductViewRepository viewRepository) {
        this.viewRepository = viewRepository;
    }
    
    @ApplicationModuleListener
    public void on(ProductCreatedEvent event) {
        ProductView view = new ProductView();
        view.setId(event.id());
        view.setName(event.name());
        view.setDescription(event.description());
        view.setPrice(event.price());
        view.setStock(event.stock());
        view.setCategoryName(event.category());
        view.setLastUpdated(LocalDateTime.now());
        viewRepository.save(view);
    }
    
    @ApplicationModuleListener
    public void on(ProductUpdatedEvent event) {
        viewRepository.findById(event.id()).ifPresent(view -> {
            view.setName(event.name());
            view.setDescription(event.description());
            view.setPrice(event.price());
            view.setCategoryName(event.category());
            view.setLastUpdated(LocalDateTime.now());
            viewRepository.save(view);
        });
    }
    
    @ApplicationModuleListener
    public void on(ProductStockChangedEvent event) {
        viewRepository.findById(event.id()).ifPresent(view -> {
            view.setStock(event.newStock());
            view.setLastUpdated(LocalDateTime.now());
            viewRepository.save(view);
        });
    }
} 