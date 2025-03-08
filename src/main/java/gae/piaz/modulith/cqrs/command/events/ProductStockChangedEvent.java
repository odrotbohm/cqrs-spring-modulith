package gae.piaz.modulith.cqrs.command.events;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ProductStockChangedEvent(
    Long id,
    Integer newStock,
    Integer stockChange,
    LocalDateTime updatedAt
) {} 