package es.test.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Message sources for doing internationalization application info.
 *
 */
@Configuration
public class MessagesConfig {

  /**
   * <p>Getting customized messages.</p>
   *
   * @return a {@link ResourceBundleMessageSource} object.
   */
  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    source.setBasenames("messages/ErrorsMessages");
    source.setUseCodeAsDefaultMessage(true);
    return source;
  }

}
