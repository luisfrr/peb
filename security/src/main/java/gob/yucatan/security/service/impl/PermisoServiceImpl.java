package gob.yucatan.security.service.impl;

import gob.yucatan.security.entity.Permiso;
import gob.yucatan.security.entity.Permiso_;
import gob.yucatan.security.repository.IPermisoRepository;
import gob.yucatan.security.service.iface.IPermisoService;
import gob.yucatan.utilities.specification.CustomSpecification;
import gob.yucatan.utilities.specification.EntitySpecification;
import gob.yucatan.utilities.specification.FilterOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermisoServiceImpl implements IPermisoService {

    private final IPermisoRepository permisoRepository;

    @Override
    public List<Permiso> findAllDynamic(Permiso permiso) {
        EntitySpecification<Permiso> specification = new EntitySpecification<>();

        if(permiso.getNombre() != null && !permiso.getNombre().isEmpty()){
            specification.add(new CustomSpecification(FilterOperation.MATCH,
                    permiso.getNombre(),
                    Permiso_.NOMBRE));
        }

        return permisoRepository.findAll(specification);
    }

    @Override
    public List<Permiso> findAll() {
        return permisoRepository.findAll();
    }

    @Override
    public void updateAll(List<Permiso> permisos) {

        List<Permiso> permisoDbList = permisoRepository.findAll();

        for(Permiso permiso : permisos) {
            permisoDbList.stream().filter(p -> Objects.equals(p.getCodigo(), permiso.getCodigo()))
                    .map(Permiso::getIdPermiso)
                    .findFirst()
                    .ifPresent(permiso::setIdPermiso);

            if(permiso.getSubPermisos() != null && !permiso.getSubPermisos().isEmpty()){
                for(Permiso subPermiso : permiso.getSubPermisos()){
                    permisoDbList.stream().filter(p -> Objects.equals(p.getCodigo(), subPermiso.getCodigo()))
                            .map(Permiso::getIdPermiso)
                            .findFirst()
                            .ifPresent(subPermiso::setIdPermiso);
                }
            }
        }

        permisoRepository.saveAll(permisos);
    }
}
