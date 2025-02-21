package com.itti.digital.atm.atm_authorization_api.configurations;

import org.springframework.boot.test.context.TestConfiguration;

import org.testcontainers.containers.BindMode;

import org.wiremock.integrations.testcontainers.WireMockContainer;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	public static WireMockContainer wireMockContainer = new WireMockContainer("wiremock/wiremock")
			.withClasspathResourceMapping("wiremock", "/home/wiremock", BindMode.READ_ONLY);

	/*public static MSSQLServerContainer<?> sqlServerContainer = new MSSQLServerContainer<>(DockerImageName.parse("mcr.microsoft.com/mssql/server:latest"))
			.acceptLicense();
*/
	//public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest")).
	//		.acceptLicense();

	/*@Bean
	@ServiceConnection
	MongoDBContainer mongoDbContainer() {
		return new MongoDBContainer(DockerImageName.parse("mongo:latest"));
	}
*/
	static {
		wireMockContainer.start();
		//sqlServerContainer.start();
		System.setProperty("spring.profiles.active", "local");
		System.setProperty("wiremock.atm-services", wireMockContainer.getBaseUrl());
		//System.setProperty("spring.datasource.url", sqlServerContainer.getJdbcUrl());
		//System.setProperty("spring.datasource.username", sqlServerContainer.getUsername());
		//System.setProperty("spring.datasource.password", sqlServerContainer.getPassword());

	}

}
