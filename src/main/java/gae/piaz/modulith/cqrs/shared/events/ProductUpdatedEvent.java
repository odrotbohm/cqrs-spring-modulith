package gae.piaz.modulith.cqrs.shared.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductUpdatedEvent(
    Long id,
    String name,
    String description,
    BigDecimal price,
    String category,
    LocalDateTime updatedAt
) {} 