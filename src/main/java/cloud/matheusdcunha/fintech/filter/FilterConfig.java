package cloud.matheusdcunha.fintech.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final IpFilter ipFilter;


    public FilterConfig(IpFilter ipFilter) {
        this.ipFilter = ipFilter;
    }

    @Bean
    public FilterRegistrationBean<IpFilter> filterFilterRegistrationBean(){
        var registrationBean = new FilterRegistrationBean<IpFilter>();

        registrationBean.setFilter(this.ipFilter);
        registrationBean.setOrder(1);

        return registrationBean;
    }

}
