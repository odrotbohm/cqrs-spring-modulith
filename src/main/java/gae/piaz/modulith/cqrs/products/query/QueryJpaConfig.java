package gae.piaz.modulith.cqrs.products.query;

import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = "gae.piaz.modulith.cqrs.products.query",
		entityManagerFactoryRef = "queryEntityManagerFactory",
		transactionManagerRef = "queryTransactionManager")
class QueryJpaConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.query")
	DataSource queryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "queryEntityManagerFactory")
	LocalContainerEntityManagerFactoryBean queryEntityManager(EntityManagerFactoryBuilder builder,
			@Qualifier("queryDataSource") DataSource dataSource) {

		return builder
				.dataSource(dataSource)
				.packages("gae.piaz.modulith.cqrs.products.query")
				.persistenceUnit("query")
				.build();
	}

	@Bean(name = "queryTransactionManager")
	PlatformTransactionManager queryTransactionManager(
			@Qualifier("queryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
