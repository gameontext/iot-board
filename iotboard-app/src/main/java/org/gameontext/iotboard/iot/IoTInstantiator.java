package org.gameontext.iotboard.iot;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class IoTInstantiator implements ServletContextListener {

    @Inject IoTAppClient appClient;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Starting the app client");
        appClient.toString();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
