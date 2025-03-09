package gob.yucatan.security.service.impl;

import gob.yucatan.security.annotations.ConfigPermiso;
import gob.yucatan.security.annotations.ConfigPermisoArray;
import gob.yucatan.security.entity.Permiso;
import gob.yucatan.security.service.iface.IPermisoScannerService;
import gob.yucatan.utilities.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermisoScannerServiceImpl implements IPermisoScannerService {

    @Override
    public List<Permiso> getPermisos(String scanPackage, String userName) {

        List<Permiso> permisos = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider permisoProvider = createPermisoScanner();

        Set<BeanDefinition> permisoBeanDefinition = permisoProvider.findCandidateComponents(scanPackage);
        for(BeanDefinition beanDefinition : permisoBeanDefinition) {
            Permiso permiso = this.getPermisoInfo(beanDefinition, userName);
            permisos.add(permiso);
        }

        return permisos;
    }

    private Permiso getPermisoInfo(BeanDefinition beanDefinition, String userName) {

        try {
            Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
            String packagePath = clazz.getPackageName();
            String className = clazz.getSimpleName();

            ConfigPermiso configPermiso = clazz.getAnnotation(ConfigPermiso.class);

            Permiso permiso = Permiso.builder()
                    .codigo(configPermiso.codigo())
                    .nombre(configPermiso.nombre())
                    .descripcion(configPermiso.descripcion())
                    .url(configPermiso.url())
                    .tipoPermiso(configPermiso.tipo())
                    .orden(configPermiso.orden())
                    .className(packagePath + "." + className)
                    .methodName(null)
                    .fechaCreacion(new Date())
                    .creadoPor(userName)
                    .fechaModificacion(new Date())
                    .modificadoPor(userName)
                    .build();

            permiso.setSubPermisos(getSubPermisos(permiso, clazz, userName));

            return permiso;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalErrorException("No se pudo encontrar el permiso", e);
        }
    }

    private Set<Permiso> getSubPermisos(Permiso permisoParent, Class<?> clazz, String userName) {
        Set<Permiso> subPermisos = new HashSet<>();
        try {
            for(Method method : clazz.getDeclaredMethods()) {

                if(method.isAnnotationPresent(ConfigPermiso.class)) {

                    String packagePath = clazz.getPackageName();
                    String className = clazz.getSimpleName();

                    ConfigPermiso configPermiso = method.getAnnotation(ConfigPermiso.class);

                    subPermisos.add(Permiso.builder()
                                .codigo(configPermiso.codigo())
                                .nombre(configPermiso.nombre())
                                .descripcion(configPermiso.descripcion())
                                .url(configPermiso.url())
                                .tipoPermiso(configPermiso.tipo())
                                .orden(configPermiso.orden())
                                .permisoParent(permisoParent)
                                .className(packagePath + "." + className)
                                .methodName(method.getName())
                                .fechaCreacion(new Date())
                                .creadoPor(userName)
                                .fechaModificacion(new Date())
                                .modificadoPor(userName)
                            .build());
                }

                if(method.isAnnotationPresent(ConfigPermisoArray.class)) {
                    String packagePath = clazz.getPackageName();
                    String className = clazz.getSimpleName();

                    ConfigPermisoArray configPermisoArray = method.getAnnotation(ConfigPermisoArray.class);

                    for(ConfigPermiso configPermiso : configPermisoArray.value()) {
                        subPermisos.add(Permiso.builder()
                                .codigo(configPermiso.codigo())
                                .nombre(configPermiso.nombre())
                                .descripcion(configPermiso.descripcion())
                                .url(configPermiso.url())
                                .tipoPermiso(configPermiso.tipo())
                                .orden(configPermiso.orden())
                                .permisoParent(permisoParent)
                                .className(packagePath + "." + className)
                                .methodName(method.getName())
                                .fechaCreacion(new Date())
                                .creadoPor(userName)
                                .fechaModificacion(new Date())
                                .modificadoPor(userName)
                                .build());
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalErrorException("No se pudo encontrar los subpermisos.", e);
        }

        return subPermisos;
    }

    private ClassPathScanningCandidateComponentProvider createPermisoScanner() {
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(ConfigPermiso.class));
        return provider;
    }
}
