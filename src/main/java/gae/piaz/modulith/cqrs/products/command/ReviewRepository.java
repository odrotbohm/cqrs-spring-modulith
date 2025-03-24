package gae.piaz.modulith.cqrs.products.command;

import org.springframework.data.repository.CrudRepository;

interface ReviewRepository extends CrudRepository<Review, ReviewIdentifier> {

}
