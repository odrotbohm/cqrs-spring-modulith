package gae.piaz.modulith.cqrs.products.command;

import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = { "gae.piaz.modulith.cqrs.products.command", "org.springframework.modulith.events.jpa" },
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager")
class CommandJpaConfig {

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.command")
	DataSource commandDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "entityManagerFactory")
	LocalContainerEntityManagerFactoryBean commandEntityManager(EntityManagerFactoryBuilder builder,
			@Qualifier("commandDataSource") DataSource dataSource) {

		return builder
				.dataSource(dataSource)
				.packages("gae.piaz.modulith.cqrs.products.command", "org.springframework.modulith.events.jpa")
				.persistenceUnit("command")
				.build();
	}

	@Primary
	@Bean(name = "transactionManager")
	PlatformTransactionManager commandTransactionManager(
			@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
