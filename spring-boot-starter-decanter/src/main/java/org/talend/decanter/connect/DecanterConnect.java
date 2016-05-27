package org.talend.decanter.connect;

import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.apache.felix.connect.launch.BundleDescriptor;
import org.apache.felix.connect.launch.ClasspathScanner;
import org.apache.felix.connect.launch.PojoServiceRegistry;
import org.apache.felix.connect.launch.PojoServiceRegistryFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

@org.springframework.context.annotation.Configuration
public class DecanterConnect {
    public static String[] bundleNames = new String[]{
                                                      "org.apache.felix.connect",
                                                      "org.apache.felix.scr",
                                                      "org.apache.karaf.decanter.appender.log",
                                                      "org.apache.karaf.decanter.appender.kafka",
                                                      "org.apache.karaf.decanter.api",
                                                      "org.apache.felix.eventadmin",
                                                      "org.apache.felix.configadmin",
                                                      "org.apache.karaf.decanter.marshaller.json"};
    private PojoServiceRegistry registry;
    
    public DecanterConnect() throws Exception {
        registry = createRegistry();
        configureEventAdmin(registry.getBundleContext());
    }

    private PojoServiceRegistry createRegistry() throws Exception {
        ServiceLoader<PojoServiceRegistryFactory> loader = ServiceLoader.load( PojoServiceRegistryFactory.class );
        PojoServiceRegistryFactory srFactory = loader.iterator().next();
        HashMap<String, Object> pojoSrConfig = new HashMap<>();
        String filter = getBundleFilter();
        List<BundleDescriptor> bundles = new ClasspathScanner().scanForBundles(filter);
        bundles.stream().forEach(desc -> System.out.println(desc.getHeaders().get(Constants.BUNDLE_SYMBOLICNAME)));
        pojoSrConfig.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS, bundles);
        System.out.println(bundles.size());
        pojoSrConfig.put("felix.cm.dir", "config");
        PojoServiceRegistry registry = srFactory.newPojoServiceRegistry( pojoSrConfig );
        Dictionary<String, String> kafka = new Hashtable<>();
        kafka.put("bootstrap.servers", "kafka:9092");
        
        configure(registry.getBundleContext(), "org.apache.karaf.decanter.appender.kafka", kafka);
        return registry;
    }

    private static void configure(BundleContext context, String pid, Dictionary<String, String> properties) throws IOException {
        ServiceTracker<ConfigurationAdmin, ConfigurationAdmin> tracker = new ServiceTracker<>(context, ConfigurationAdmin.class, null);
        tracker.open();
        ConfigurationAdmin configAdmin = tracker.getService();
        Configuration config = configAdmin.getConfiguration(pid);
        config.update(properties);
        tracker.close();
    }

    private static String getBundleFilter() {
        String joined = Arrays.asList(bundleNames).stream().collect(Collectors.joining(")(Bundle-SymbolicName="));
        return "(|(Bundle-SymbolicName=" + joined + "))";
    }

    private static void configureEventAdmin(BundleContext context) {
        ServiceTracker<EventAdmin, EventAdmin> tracker = new ServiceTracker<>(context, EventAdmin.class, null);
        tracker.open();
        EventAdmin eventAdmin = tracker.getService();
        LogbackDecanterAppender.setDispatcher(eventAdmin);
        tracker.close();
    }

    @PreDestroy
    public void close() {
        try {
            registry.getBundleContext().getBundle(0).stop();
        } catch (BundleException e) {
        }
    }
}
