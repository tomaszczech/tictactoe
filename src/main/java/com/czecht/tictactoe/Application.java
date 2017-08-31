package com.czecht.tictactoe;

import org.ocpsoft.rewrite.servlet.RewriteFilter;
import org.primefaces.push.PushServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;
import java.util.EnumSet;

import com.czecht.tictactoe.shared.LoginFilter;

@EnableAutoConfiguration
@ComponentScan({ "com.czecht.tictactoe" })
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		FacesServlet servlet = new FacesServlet();
		return new ServletRegistrationBean(servlet, "*.jsf");
	}

	@Bean
	public ServletRegistrationBean pushServletRegistrationBean() {
		ServletRegistrationBean pushServlet = new ServletRegistrationBean(new PushServlet(), "/primepush/*");
		pushServlet.addInitParameter("org.atmosphere.annotation.packages", "org.primefaces.push");
		pushServlet.addInitParameter("org.atmosphere.cpr.packages", "com.czecht.tictactoe.infrastructure.push");
		pushServlet.setAsyncSupported(true);
		pushServlet.setLoadOnStartup(0);
		pushServlet.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return pushServlet;
	}

	@Bean
	public FilterRegistrationBean rewriteFilter() {
		FilterRegistrationBean rwFilter = new FilterRegistrationBean(new RewriteFilter());
		rwFilter.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST,
				DispatcherType.ASYNC, DispatcherType.ERROR));
		rwFilter.addUrlPatterns("/*");
		return rwFilter;
	}

	@Bean
	public FilterRegistrationBean loginFilter() {
		FilterRegistrationBean rwFilter = new FilterRegistrationBean(new LoginFilter());
		rwFilter.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST,
				DispatcherType.ASYNC,
				DispatcherType.ERROR));
		rwFilter.addUrlPatterns("/game/*");
		return rwFilter;
	}
}
