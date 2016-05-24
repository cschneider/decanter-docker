package net.lr.tasklist.persistence.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ServiceLoader;

import org.apache.felix.connect.launch.BundleDescriptor;
import org.apache.felix.connect.launch.ClasspathScanner;
import org.apache.felix.connect.launch.PojoServiceRegistry;
import org.apache.felix.connect.launch.PojoServiceRegistryFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Controller
public class Application {

    public static void main(String[] args) throws Exception {
        ServiceLoader<PojoServiceRegistryFactory> loader = ServiceLoader.load( PojoServiceRegistryFactory.class );
        PojoServiceRegistryFactory srFactory = loader.iterator().next();
        HashMap<String, Object> pojoSrConfig = new HashMap<>();
        pojoSrConfig.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS, new ClasspathScanner().scanForBundles());
        PojoServiceRegistry registry = srFactory.newPojoServiceRegistry( pojoSrConfig );
        System.out.println(registry);
        //SpringApplication.run(Application.class, args);
    }

}
