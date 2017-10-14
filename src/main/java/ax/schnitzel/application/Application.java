package ax.schnitzel.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * The application's entry point
 * 
 * @see {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter}
 */
@ComponentScan(basePackages = { "ax.schnitzel" })
@EnableAutoConfiguration
public class Application extends WebMvcConfigurerAdapter {

	/**
	 * @param args command-line arguments
	 */
	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {

		// Add WebJars support
		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
	}

}
