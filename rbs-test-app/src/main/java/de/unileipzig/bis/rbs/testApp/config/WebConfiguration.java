package de.unileipzig.bis.rbs.testApp.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring web configuration, contains beans to configure the web app
 *
 * @author Lukas Werner
 */
@Configuration
public class WebConfiguration {

    /**
     * Bean for the H2 console servlet
     *
     * @return the bean
     */
    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

}