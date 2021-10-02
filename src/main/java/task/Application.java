package task;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import task.config.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class Application implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootConfig = new AnnotationConfigWebApplicationContext();
        rootConfig.register(RootConfig.class, SecurityConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootConfig));

        AnnotationConfigWebApplicationContext jspContext = new AnnotationConfigWebApplicationContext();
        jspContext.register(WebJspConfig.class);

        ServletRegistration.Dynamic jspServlet = servletContext.addServlet("jspServlet",
                new DispatcherServlet(jspContext));
        jspServlet.addMapping("/home");

        AnnotationConfigWebApplicationContext restContext = new AnnotationConfigWebApplicationContext();
        restContext.register(RestConfig.class);

        ServletRegistration.Dynamic restServlet = servletContext.addServlet("restServlet", new DispatcherServlet(restContext));
        restServlet.addMapping("/");

        servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy("springSecurityFilterChain")).addMappingForUrlPatterns(null, true, "/*");
    }
}
