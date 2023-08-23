package fr.lelouet;

import fr.lelouet.services.scheduler.ScheduledJobs;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.lelouet.guice.ApplicationModule;

import com.coreoz.plume.jersey.guice.JerseyGuiceFeature;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

/**
 * The application entry point, where it all begins.
 */
public class WebApplication {
    private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) {
        try {
            long startTimestamp = System.currentTimeMillis();

            // initialize all application objects with Guice
            Injector injector = Guice.createInjector(Stage.PRODUCTION, new ApplicationModule());

            ResourceConfig jerseyResourceConfig = injector.getInstance(ResourceConfig.class);
            // enable Jersey to create objects through Guice Injector instance
            jerseyResourceConfig.register(new JerseyGuiceFeature(injector));

            // schedule tasks
            injector.getInstance(ScheduledJobs.class).scheduleJobs();

            logger.info("Server started in {} ms", System.currentTimeMillis() - startTimestamp);
        } catch (Throwable e) {
            logger.error("Failed to start server", e);
            // This line is important, because during initialization some libraries change the main thread type
            // to daemon, which mean that even if the project is completely down, the JVM is not stopped.
            // Stopping the JVM is important to enable production supervision tools to detect and restart the project.
            System.exit(1);
        }
    }
}
