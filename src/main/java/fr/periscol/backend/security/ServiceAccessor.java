package fr.periscol.backend.security;

import fr.periscol.backend.service.service_model.ServiceMetadataService;
import fr.periscol.backend.web.rest.errors.NotFoundAlertException;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Expression to check if a user has the right to access to the service
 */
@Component
public class ServiceAccessor {

    private final ServiceMetadataService serviceMetadata;

    /**
     * Creates a new instance
     */
    public ServiceAccessor(ServiceMetadataService serviceMetadata) {
        this.serviceMetadata = serviceMetadata;
    }

    /**
     * @param serviceId a service ID
     * @return true if the current user has the specified service permission, false otherwise.
     */
    public boolean hasServiceAccess(Long serviceId) {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            final var authOpt = serviceMetadata.findOne(serviceId);
            if (authOpt.isEmpty())
                throw new NotFoundAlertException("Requested service does not exist.");
            return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(authOpt.get().getName()));
        }
        return false;
    }
}
