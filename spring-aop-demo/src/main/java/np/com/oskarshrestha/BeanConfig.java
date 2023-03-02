package np.com.oskarshrestha;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "np.com.oskarshrestha")
@EnableAspectJAutoProxy
public class BeanConfig {

}
