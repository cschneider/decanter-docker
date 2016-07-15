package org.apache.karaf.activemq;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

@Component //
( //
    configurationPid = "org.apache.karaf.activemq", //
    immediate = true, //
    configurationPolicy = ConfigurationPolicy.REQUIRE //
)
public class ConnectionFactoryProvider {

    private static final String OSGI_JNDI_SERVICE_NAME = "osgi.jndi.service.name";
    private ServiceRegistration<ConnectionFactory> reg;

    @Activate
    public void create(ComponentContext compContext) {
        BundleContext context = compContext.getBundleContext();
        Dictionary<String, Object> config = compContext.getProperties();
        String brokerURL = getString(config, "url", "tcp://localhost:61616");
        String jndiName = getString(config, OSGI_JNDI_SERVICE_NAME, "jms/local");
        String userName = getString(config, "userName", null);
        String password = getString(config, "password", null);
        long expiryTimeout = new Long(getString(config, "expiryTimeout", "0"));
        int idleTimeout = new Integer(getString(config, "idleTimeout", "30000"));
        int maxConnections = new Integer(getString(config, "maxConnections", "8"));
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(brokerURL);
        if (userName != null) {
            cf.setUserName(userName);
            cf.setPassword(password);
        }
        PooledConnectionFactory pcf = new PooledConnectionFactory();
        pcf.setConnectionFactory(cf);
        pcf.setExpiryTimeout(expiryTimeout);
        pcf.setIdleTimeout(idleTimeout);
        pcf.setMaxConnections(maxConnections);
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put(OSGI_JNDI_SERVICE_NAME, jndiName);
        reg = context.registerService(ConnectionFactory.class, pcf, props);
    }
    
    @Deactivate
    public void deactivate() {
        reg.unregister();
    }

    private String getString(Dictionary<String, Object> config, String key, String defaultValue) {
        Object value = config.get(key);
        return value != null ? value.toString() : defaultValue;
    }
}
