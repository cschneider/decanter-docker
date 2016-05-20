package net.lr.tasklist.persistence.impl;

import javax.inject.Named;

import org.glassfish.jersey.server.ResourceConfig;

@Named
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(TaskServiceRest.class);
    }
}
