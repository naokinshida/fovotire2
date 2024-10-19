package com.example.nagoyameshi;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
/*import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;*/
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /*@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/favicon.ico").setViewName("forward:/resources/static/favicon.ico");
    } */   

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/");
                
        // 明示的に favicon へのハンドリングを追加
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/favicon.ico", "classpath:/resources/static/favicon.ico");
    }

}
