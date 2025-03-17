package gae.piaz.modulith.cqrs.command.events;

import java.math.BigDecimal;

public record ProductCreatedEvent(Long id, String name, String description, BigDecimal price, Integer stock, String category) {

}