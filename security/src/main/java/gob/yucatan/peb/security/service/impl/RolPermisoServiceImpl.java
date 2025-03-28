package gob.yucatan.peb.security.service.impl;

import gob.yucatan.peb.security.entity.Permiso;
import gob.yucatan.peb.security.entity.Rol;
import gob.yucatan.peb.security.entity.RolPermiso;
import gob.yucatan.security.entity.*;
import gob.yucatan.peb.security.enums.EstatusPermiso;
import gob.yucatan.peb.security.repository.IRolPermisoRepository;
import gob.yucatan.peb.security.service.iface.IPermisoService;
import gob.yucatan.peb.security.service.iface.IRolPermisoService;
import gob.yucatan.peb.utilities.specification.CustomSpecification;
import gob.yucatan.peb.utilities.specification.EntitySpecification;
import gob.yucatan.peb.utilities.specification.FilterOperation;
import jakarta.persistence.Transient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolPermisoServiceImpl implements IRolPermisoService {

    private final IRolPermisoRepository rolPermisoRepository;
    private final IPermisoService permisoService;
//    private final IBitacoraRolService bitacoraRolService;

    @Override
    public List<RolPermiso> findAllDynamic(RolPermiso rolPermiso) {

        EntitySpecification<RolPermiso> specification = new EntitySpecification<>();

        if(rolPermiso.getRol() != null && rolPermiso.getRol().getIdRol() != null)
            specification.add(new CustomSpecification(FilterOperation.EQUAL,
                    rolPermiso.getRol().getIdRol(),
                    RolPermiso_.ROL, Rol_.ID_ROL));

        if(rolPermiso.getPermisoParentId() != null)
            specification.add(new CustomSpecification(FilterOperation.EQUAL,
                    rolPermiso.getPermisoParentId(),
                    RolPermiso_.PERMISO, Permiso_.PERMISO_PARENT, Permiso_.ID_PERMISO));

        if(rolPermiso.getEstatusPermiso() != null)
            specification.add(new CustomSpecification(FilterOperation.EQUAL,
                    rolPermiso.getEstatusPermiso(),
                    RolPermiso_.ESTATUS_PERMISO));

        return rolPermisoRepository.findAll(specification);
    }

    @Override
    public List<RolPermiso> findByRol(Rol rol, Permiso permisoFilter) {

        // Se buscan todos los permisos activos de la aplicacion
        List<Permiso> permisoList = permisoService.findAllDynamic(Permiso.builder()
//                .estatus(EstatusRegistro.ACTIVO)
                .nombre(permisoFilter.getNombre())
                .build());

        // Se buscan todos los permisos que tiene asignado el rol
        List<RolPermiso> rolPermisoList = this.findAllDynamic(RolPermiso.builder().rol(rol).build());

        // Se obtiene una lista de los permisos que no han sido registrados
        List<Permiso> permisosNoRegistrados = permisoList.stream()
                .filter(permiso -> rolPermisoList.stream()
                        .noneMatch(rolPermiso -> rolPermiso.getPermiso().equals(permiso)))
                .toList();

        // Se agregan todos los permisos no registrados en la lista RolPermiso
        for (Permiso permisoNoRegistrado : permisosNoRegistrados) {
            rolPermisoList.add(RolPermiso.builder()
                            .rol(rol)
                            .permiso(permisoNoRegistrado)
                            .estatusPermiso(EstatusPermiso.NO_ASIGNADO)
                    .build());
        }

        List<RolPermiso> rolPermisoParents = rolPermisoList.stream()
                .filter(rolPermiso -> rolPermiso.getPermiso().getPermisoParent() == null)
                .sorted(Comparator.comparing(r -> r.getPermiso().getNombre()))
                .toList();

        for (RolPermiso rolPermisoParent : rolPermisoParents) {
            List<RolPermiso> subRolPermisoList = rolPermisoList.stream()
                    .filter(rp -> Objects.equals(rp.getPermiso().getPermisoParent(), rolPermisoParent.getPermiso()))
                    .sorted(Comparator.comparing((RolPermiso r) -> r.getPermiso().getTipoPermiso())
                            .thenComparing((RolPermiso r) -> r.getPermiso().getOrden()))
                    .toList();

            rolPermisoParent.setSubRolPermisoList(subRolPermisoList);
        }

        return rolPermisoParents;
    }

    @Override
    @Transient
    public void asignarPermiso(RolPermiso rolPermiso, String userName) {

        Long id = rolPermiso.getIdRolPermiso() != null ? rolPermiso.getIdRolPermiso() : 0;
        Optional<RolPermiso> rolPermisoOptional = this.rolPermisoRepository.findById(id);

        // Si es un permiso padre
        if(rolPermiso.getPermiso().getPermisoParent() == null) {

            // Y si su estatus cambio a NO_ASIGNADO o DESHABILITADO
            if(rolPermiso.getEstatusPermiso() == EstatusPermiso.NO_ASIGNADO ||
                    rolPermiso.getEstatusPermiso() == EstatusPermiso.DESHABILITADO) {

                List<RolPermiso> rolPermisoAnteriorList = new ArrayList<>();
                rolPermisoOptional.ifPresent(rolPermisoAnteriorList::add);

                // Se buscan los subPermisos de ese permiso y es rol
                List<RolPermiso> subPermisos = findAllDynamic(RolPermiso.builder()
                        .rol(rolPermiso.getRol())
                        .permisoParentId(Long.valueOf(rolPermiso.getPermiso().getIdPermiso()))
                        .build());

                List<RolPermiso> subPermisosToUpdate = findAllDynamic(RolPermiso.builder()
                        .rol(rolPermiso.getRol())
                        .permisoParentId(Long.valueOf(rolPermiso.getPermiso().getIdPermiso()))
                        .build());

                // Se agregan los subPermisos
                rolPermisoAnteriorList.addAll(subPermisos);

                // Y se marcan en NO_ASIGNADO
                subPermisosToUpdate.forEach(subPermiso -> subPermiso.setEstatusPermiso(EstatusPermiso.NO_ASIGNADO));

//                List<RolPermiso> rolPermisoNuevoList = new ArrayList<>();
//                rolPermisoNuevoList.add(rolPermiso);
//                rolPermisoNuevoList.addAll(subPermisosToUpdate);

//                // Se guarda la bitacora
//                bitacoraRolService.guardarBitacoraPermisos(rolPermiso.getRol(), rolPermisoAnteriorList,
//                        rolPermisoNuevoList, userName);

                // Se actualiza el permiso principal
                rolPermisoRepository.save(rolPermiso);

                // Se actualizan los subPermisos en la DB
                rolPermisoRepository.saveAll(subPermisosToUpdate);
            } else {

//                List<RolPermiso> rolPermisoAnteriorList = new ArrayList<>();
//                rolPermisoOptional.ifPresent(rolPermisoAnteriorList::add);
//
//                List<RolPermiso> rolPermisoNuevoList = List.of(rolPermiso);

//                // Se guarda la bitacora
//                bitacoraRolService.guardarBitacoraPermisos(rolPermiso.getRol(), rolPermisoAnteriorList,
//                        rolPermisoNuevoList, userName);

                rolPermisoRepository.save(rolPermiso);

            }
        } else {

//            List<RolPermiso> rolPermisoAnteriorList = new ArrayList<>();
//            rolPermisoOptional.ifPresent(rolPermisoAnteriorList::add);
//
//            List<RolPermiso> rolPermisoNuevoList = List.of(rolPermiso);

//            // Se guarda la bitacora
//            bitacoraRolService.guardarBitacoraPermisos(rolPermiso.getRol(), rolPermisoAnteriorList,
//                    rolPermisoNuevoList, userName);

            rolPermisoRepository.save(rolPermiso);
        }
    }
}
