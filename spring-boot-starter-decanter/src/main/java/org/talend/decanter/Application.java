package org.talend.decanter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.decanter.connect.DecanterConnect;

public class Application {
    static Logger LOG = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) throws Exception {
        LOG.info("test");
        DecanterConnect connect = new DecanterConnect();
        LOG.info("test2");
        System.in.read();
        connect.close();
    }

}
