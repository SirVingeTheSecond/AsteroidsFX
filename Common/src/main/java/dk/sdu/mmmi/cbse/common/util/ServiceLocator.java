package dk.sdu.mmmi.cbse.common.util;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service locator utility to centralize service discovery logic and error handling.
 * Provides robust mechanisms for finding service implementations and fallbacks.
 */
public class ServiceLocator {
    private static final Logger LOGGER = Logger.getLogger(ServiceLocator.class.getName());

    /**
     * Get a service implementation with proper error reporting.
     *
     * @param <T> Type of service interface
     * @param serviceClass The service interface class
     * @return The service implementation
     * @throws ServiceNotFoundException If no implementation is found
     */
    public static <T> T getService(Class<T> serviceClass) {
        return getService(serviceClass, null);
    }

    /**
     * Get a service implementation with a fallback in case none is found.
     *
     * @param <T> Type of service interface
     * @param serviceClass The service interface class
     * @param fallback Fallback implementation or null
     * @return The service implementation or fallback
     * @throws ServiceNotFoundException If no implementation is found and fallback is null
     */
    public static <T> T getService(Class<T> serviceClass, T fallback) {
        LOGGER.log(Level.INFO, "Looking for service: {0}", serviceClass.getName());

        Optional<T> service = ServiceLoader.load(serviceClass).findFirst();

        if (service.isPresent()) {
            LOGGER.log(Level.INFO, "Found service implementation: {0}",
                    service.get().getClass().getName());
            return service.get();
        } else {
            if (fallback != null) {
                LOGGER.log(Level.WARNING,
                        "No implementation found for {0}, using fallback",
                        serviceClass.getName());
                return fallback;
            }

            LOGGER.log(Level.SEVERE,
                    "No implementation found for required service: {0}",
                    serviceClass.getName());
            throw new ServiceNotFoundException("No implementation found for required service: " +
                    serviceClass.getName());
        }
    }

    /**
     * Exception thrown when a required service cannot be found.
     */
    public static class ServiceNotFoundException extends RuntimeException {
        public ServiceNotFoundException(String message) {
            super(message);
        }
    }
}