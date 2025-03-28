# CQRS with Spring Modulith

The repository here contains a refactored variant of the code base for [this blog post](https://gaetanopiazzolla.github.io/java/design-patterns/springboot/2025/03/17/cqrs.html). It uses the same fundamental abstractions but tries to follow Spring Modulith-idiomatic code arrangements and jMolecules to make the code a bit more architecturally-evident.

Here are the most fundamental changes:

## Infrastructure
* I've added a `pom.xml` to build the project with Maven. This is primarily to be able to use code generation for jMolecules (see below) which I couldn't get to set up properly with Gradle.
* I've simplified the database arrangement to rather use a single datasource (see "Open Issues" below).

## Package structure

* The package structure has been revamped to feature a top-level application module `products`. This opens up the possibility for other business modules to be located as sibling packages. If the products module had to move to a different project, all we have to do is move that very package.
* The CQRS-idiomatic separation of commands and queries is expressed as sub-packages. I've abstained from further grouping architectural stereotypes (events, event handlers) to keep the package structure simple, communicate the high-level structure (business modules, hih-level technical decomposition) and, most-importantly, allow the technical elements to use visibility modifiers to hide elements from others.
* Almost all code within the `command` and `query` packages is package private, except the types in `command` that are needed by the query side. Most notably, that's `ProductIdentifier`, `ReviewIdentifier` and the event types.
* The outcome of all of this is, that we can control access on two different levels:
    1. None of the code in `command` and `query` is accessible to other application modules. If new modules were introduced, we can deliberately move code to the `products` package to explicitly expose it to those. This level of encapsulation is achieved by Spring Moduliths verification rules executed in `ModulithStructureTest`.
    2. Within `products` we can use plain Java visibility modifiers to control, which elements of the command side are accessible by the query side. Point 1 ensures that making a type public does not expose it to code outside the `products` module.

## Design

* I've renamed `ProductQueryController` to `ProductViewController` as it's (and should) solely (be) dealing with the `ProductView` view model.
* I've removed the `ProductQueryService` as it was primarily directly delegating to the repository. The `Optional` to exception translation has been replaced with a `ResponseEntity.of(Optional)` in the controller, so that it automatically translates empty instances into a `404 Not Found`.

## jMolecules

* I've introduced jMolecules CQRS and DDD dependencies to be able to mark aggregates (`Product`, `Review`, and `ProductView`), query models (`ProductView`) and domain event handlers (`ProductEventHandler`). While the latter two are primarily of informative nature, the former is used to generate all necessary JPA mapping metadata, which is evident from the aggregate types not carrying any JPA annotations anymore. The code generation is triggered by the ByteBuddy build plugin, which I failed to set up properly with Gradle.
* The `Review` -> `Product` relationship was remodeled to a jMolecules `Association` as aggregates must only depend on other aggregates via identifier. 

## API exposure

* I've removed the command / query distinction from the URI space as it's an implementation detail and shouldn't be leaked to clients. That said, it might make sense to add ArchUnit rules, that verifies only `@GetMapping`s being used on the query side.

## Open issues

* The persistence setup was (needlessly) complex and easy to misuse. While working with two data sources prevents code in either of the command / query arrangement to access the other's data, the boilerplate introduced is significant. A `DataSource`, `EntityManager`, and `PlatformTransactionManager` triple has to be set up for both commands and queries.

  While the original setup causes the repositories wired to the correct side, a couple of (undetected) misconfigured places: the `@Transactional` on `ProductQueryService` does not define the transaction manager reference and thus causes transactions on the command database opened needlessly. The same applies to the `@ApplicationModuleListener`s defined in `ProductEventHandler`.

  While both mistakes do not cause any errors, they needlessly consume resources and reveal that the arrangement is easy to misconfigure. The simpler, single `DataSource` setup removed both `CommandJpaConfig`, `QueryJpaConfig`, the data source declarations in `application.yaml` and the explicit transaction manager references in the `@Transactional` configurations on `ProductEventHandler` and `ProductQueryController`. The previous, albeit structurally cleaned up variant is available in the `alt/separate-datasources` branch. I'd recommend moving to that variant only if actual technical reasons such as independent scalability of the read VS. write side are required.

* The bi-directional relationship between `Product` and `Review` is something that could (should?) be removed. Aggregates should be materialized as one and a growing list of reviews could make this expensive. That'd be an argument to remove the `reviews` from the `Product`. On the other hand, keeping that list, would allow removing the inverse association and the `ReviewRepository` alltogether. Adding a review would require to lookup the `Product` adding the review to it and saving it back.
* Stereotyping `ProductView` as `AggregateRoot` is a bit anti-idiomatic. Only the latter are managed by repositories. Due to the lack of a better programming model to access query model instances, I decided to live with that tension, especially as that conceptual inconsistency is an implementation detail and not leaking to the outside.
* Classic CQRS usually encapsulates the details of a command into a type. `ProductCommandService`'s method invocation take individual parameters. Unless there's generic command handling in place, wrapping those into command objects is not strictly necessary. The conceptual inconsistency might just add a bit of cognitive load. Personally, I prefer simple method invocations as they avoid the additional boilerplate.
