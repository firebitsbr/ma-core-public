/**
 * Copyright (C) 2014 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.serotonin.m2m2.web.mvc.rest.swagger;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author Terry Packer
 *
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig{
	
	@Autowired
	private TypeResolver typeResolver;
	
	@Bean
	public Docket describe(){
		
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
		        .select()
		          .apis(RequestHandlerSelectors.any())
		          .paths(PathSelectors.any())
		          //.paths(PathSelectors.regex("/rest/" + Common.envProps.getString("swagger.mangoApiVersion", "v1") + "/.*"))
		          
		          .build(); //.pathProvider(new MangoSwaggerPathProvider());
		        //.directModelSubstitute(LocalDate.class,String.class)
		        //.genericModelSubstitutes(ResponseEntity.class);
		
//		docket.alternateTypeRules(
//		            new AlternateTypeRule(typeResolver.resolve(DeferredResult.class,
//		                    typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
//		                typeResolver.resolve(WildcardType.class)))
//		        .useDefaultResponseMessages(false);
		
//		//Error Response Messages
//		List<ResponseMessage> messageList = new ArrayList<ResponseMessage>();
//		messageList.add(new ResponseMessageBuilder()
//		                .code(500)
//		                .message("500 message")
//		                .responseModel(new ModelRef("Error"))
//		                .build());
//		docket.globalResponseMessage(RequestMethod.GET, messageList)
//		        //.securitySchemes(newArrayList(apiKey()))
//		        //.securityContexts(newArrayList(securityContext()))
//		        .enableUrlTemplating(true);
//		List<Parameter> parameterList = new ArrayList<>();
//		parameterList.add(new ParameterBuilder()
//		                .name("someGlobalParameter")
//		                .description("Description of someGlobalParameter")
//		                .modelRef(new ModelRef("string"))
//		                .parameterType("query")
//		                .required(true)
//		                .build());
//		docket.globalOperationParameters(parameterList);
		docket.apiInfo(new ApiInfoBuilder()
	              .title("Mango Rest API")
	              .description("Support: <a href='http://infiniteautomation.com/forum'>Forum</a>&nbsp or &nbsp <a href='http://infiniteautomation.com/wiki/doku.php?id=graphics:api:intro'>Wiki</a>")
	              .version("2.0")
	              .termsOfServiceUrl("https://infiniteautomation.com/terms/")
	              .contact(new Contact("IAS", "https://infiniteautomation.com", "info@infiniteautomation.com"))
	              .license("Apache 2.0")
	              .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
	              .build());
		        
		return docket;
	}
}