package com.appsdeveloperblog.estore.productservice;

import com.appsdeveloperblog.estore.productservice.command.interceptor.CreateProductCommandInterceptor;
import com.appsdeveloperblog.estore.productservice.core.errorhandling.ProductsServiceEventsErrorHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDiscoveryClient
public class ProductserviceApplication {
	Logger logger = LoggerFactory.getLogger(ProductserviceApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ProductserviceApplication.class, args);
	}

	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context, CommandBus commandBus){
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
	}

	@Autowired
	public void configure(EventProcessingConfigurer config){
		config.registerListenerInvocationErrorHandler("process-group", conf-> new ProductsServiceEventsErrorHandler());
		// we do need to do custom like above class we can use predefined class provided by axon
		//config.registerListenerInvocationErrorHandler("product-group", conf-> PropagatingErrorHandler.instance());
	}

	@Bean
	public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter){
		logger.info("snapshot bean triggered");
		return new EventCountSnapshotTriggerDefinition(snapshotter,3);
	}

}
