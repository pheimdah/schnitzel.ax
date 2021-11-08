package ax.schnitzel.application;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import ax.schnitzel.domain.service.SchnitzelService;

/**
 * The application's entry point.
 * 
 * @see {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter}
 */
@ComponentScan(basePackages = { "ax.schnitzel" })
@EnableAutoConfiguration
public class Application implements CommandLineRunner {

	/** Injected dependency used for all schnitzel related operations */
	@Autowired
	SchnitzelService schnitzelService;

	/**
	 * @param args command-line arguments
	 */
	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args).close();
	}

	public void run(String... arg0) throws Exception {

		// Set up Thymeleaf
		FileTemplateResolver templateResolver = new FileTemplateResolver();
		templateResolver.setTemplateMode("HTML");
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		// Fetch restaurants, store in Thymeleaf context
		Context context = new Context();
		context.setVariable("restaurants", schnitzelService.getRestaurants());

		// Generate today's index.html from the home.html template
		Files.createDirectories(Paths.get("output"));
		Writer writer = new FileWriter("output/index.html");
		writer.write(templateEngine.process("src/main/resources/templates/home.html", context));
		writer.close();

	}
}
