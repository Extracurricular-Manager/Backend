package fr.periscol.backend.security.annotation;

import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.service.PermissionService;
import fr.periscol.backend.service.mapper.PermissionMapper;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Component
public class PermissionAggregator {

    private static final Pattern AUTHORITY_PATTERN = Pattern.compile("(?<=hasAuthority\\(['\"]).*?(?=['\"]\\))");
    private static final String PREFIX = "ROLE_";
    private static final String PACKAGE_MARK = "periscol";

    private final ApplicationContext applicationContext;
    private final PermissionService service;
    private final PermissionMapper mapper;

    public PermissionAggregator(ApplicationContext applicationContext, PermissionService service, PermissionMapper mapper) {
        this.applicationContext = applicationContext;
        this.service = service;
        this.mapper = mapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void aggregateAllPermission() {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {

            final Object obj = applicationContext.getBean(beanName);
            final Class<?> objClz = !AopUtils.isAopProxy(obj) ? obj.getClass() : AopUtils.getTargetClass(obj);
            if (objClz.getAnnotation(RestController.class) != null && objClz.getName().contains(PACKAGE_MARK)) {
                for (Method m : objClz.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(PreAuthorize.class)) {

                        final var value = m.getAnnotation(PreAuthorize.class).value();
                        var matcher = AUTHORITY_PATTERN.matcher(value);

                        final var authorities = new HashSet<>(matcher.results()
                            .map(MatchResult::group)
                            .filter(a -> !a.startsWith(PREFIX))
                            .toList()
                        );

                        authorities.stream()
                            .map(Permission::new)
                            .filter(perm -> service.findOne(perm.getName()).isEmpty())
                            .forEach(perm -> service.save(mapper.toDto(perm)));
                    }
                }
            }
        }
    }
}
