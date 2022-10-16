package ba.unsa.etf.nwt.comment_service.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class GRPCInterceptorAppConfig extends WebMvcConfigurerAdapter{
    @Autowired
    GRPCInterceptor grpcInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(grpcInterceptor);
    }
}
