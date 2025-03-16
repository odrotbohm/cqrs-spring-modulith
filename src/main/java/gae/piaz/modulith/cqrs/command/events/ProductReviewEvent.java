package gae.piaz.modulith.cqrs.command.events;

public record ProductReviewEvent(Long productId, Long reviewId, Integer vote, String comment) {

}