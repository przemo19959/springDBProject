package application.configs;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import application.configs.root.CommonConfig;
import application.configs.root.ProdConfig;
import application.configs.root.TestConfig;
import application.configs.web.WebConfig;

public class DispatcherServlet extends AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] {CommonConfig.class,ProdConfig.class,TestConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] {WebConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		servletContext.setInitParameter("spring.profiles.active", "prod");
	}
	
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter cef=new CharacterEncodingFilter();
		cef.setEncoding("utf-8");
		cef.setForceEncoding(true);
		return new Filter[] {new HiddenHttpMethodFilter(),cef};
	}
	
	
}
