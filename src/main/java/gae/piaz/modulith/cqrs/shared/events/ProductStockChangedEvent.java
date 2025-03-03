package gae.piaz.modulith.cqrs.shared.events;

import java.time.LocalDateTime;

public record ProductStockChangedEvent(
    Long id,
    Integer newStock,
    Integer stockChange,
    LocalDateTime updatedAt
) {} 