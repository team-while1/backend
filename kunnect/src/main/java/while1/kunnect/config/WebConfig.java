package while1.kunnect.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    // WebConfig를 통해 디렉토리의 매핑
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/var/www/images/");
    }

    /*
    WebSecurityConfig가 우선순위가 높아서 WebConfig의 CORS 설정 무시
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:5173",
                        "http://localhost:3000",
                        "http://localhost:3001",
                        "http://localhost:8080",
                        "http://kunnect.co.kr",
                        "http://www.kunnect.co.kr",
                        "https://kunnect.co.kr",
                        "https://www.kunnect.co.kr"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders(
                        "Authorization",
                        "Content-Type",
                        "X-Requested-With",
                        "Accept",
                        "Origin"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }
    */
}