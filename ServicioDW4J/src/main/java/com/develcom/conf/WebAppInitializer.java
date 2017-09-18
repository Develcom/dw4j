package com.develcom.conf;

//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRegistration;
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.context.ContextLoaderListener;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//import org.springframework.web.servlet.DispatcherServlet;


//public class WebAppInitializer implements WebApplicationInitializer{
public class WebAppInitializer {

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        // Create the 'root' Spring application context
//        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.register(WebMvcConfig.class);//ServiceConfig.class, JPAConfig.class, SecurityConfig.class);
// 
//        // Manage the lifecycle of the root application context
////        servletContext.addListener(new ContextLoaderListener(rootContext));
// 
//        // Create the dispatcher servlet's Spring application context
//        AnnotationConfigWebApplicationContext dispatcherServlet = new AnnotationConfigWebApplicationContext();
//        dispatcherServlet.register(WebMvcConfig.class);
//             
//        // Register and map the dispatcher servlet
//        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("WebMvcConfig", new DispatcherServlet(dispatcherServlet));
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");
//    }
    
}
