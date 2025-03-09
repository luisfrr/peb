package gob.yucatan.security.service.impl;

import gob.yucatan.security.entity.Rol;
import gob.yucatan.security.repository.IRolRepository;
import gob.yucatan.security.service.iface.IRolService;
import gob.yucatan.utilities.exception.BadRequestException;
import gob.yucatan.utilities.exception.ConflictException;
import gob.yucatan.utilities.exception.NotFoundException;
import gob.yucatan.utilities.specification.CustomFetch;
import gob.yucatan.utilities.specification.CustomSpecification;
import gob.yucatan.utilities.specification.EntitySpecification;
import gob.yucatan.utilities.specification.FilterOperation;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolServiceImpl implements IRolService {

    private final IRolRepository rolRepository;
//    private final IBitacoraRolService bitacoraRolService;

    @Override
    public List<Rol> findAllDynamic(Rol rol) {
        EntitySpecification<Rol> specification = new EntitySpecification<>();

//        if(rol.getNombre() != null && !rol.getNombre().isEmpty())
//            specification.add(new CustomSpecification(FilterOperation.MATCH,
//                    rol.getNombre(),
//                    Rol_.NOMBRE));
//
//        if(rol.getEstatus() != null)
//            specification.add(new CustomSpecification(FilterOperation.EQUAL,
//                    rol.getEstatus(),
//                    Rol_.ESTATUS));
//
//        if(rol.isLeftJoinUsuarioRolSet())
//            specification.add(new CustomFetch(JoinType.LEFT,
//                    Rol_.USUARIO_ROL_SET));

        return rolRepository.findAll(specification);
    }

    @Override
    public Optional<Rol> findById(Integer id) {
        return rolRepository.findById(id);
    }

    @Override
    @Transactional
    public void save(Rol rol) {
        if(rol.getNombre() != null && !rol.getNombre().isEmpty()) {
            // Se agrega validación si ya existe un rol con el mismo nombre
            boolean existsByNombre = rolRepository.existsByNombre(rol.getNombre());
            if(existsByNombre)
                throw new ConflictException("Ya existe un rol con el nombre: " + rol.getNombre());

            // Se agrega el código de acuerdo al nombre
            String codigoRol = "ROLE_" + rol.getNombre().replace(" ", "_").toUpperCase();
            rol.setCodigo(codigoRol);
        }

        Rol rolSaved = rolRepository.save(rol);

        // Se guarda en la bitacora
//        bitacoraRolService.guardarBitacora("Nuevo", null, rolSaved, rol.getCreadoPor());
    }

    @Override
    @Transactional
    public void delete(Rol rol) {
        Integer ID_ROL_OWNER = 1;
        if(Objects.equals(rol.getIdRol(), ID_ROL_OWNER))
            throw new BadRequestException("No es posible eliminar el rol propietario.");

        Optional<Rol> rolOptional = rolRepository.findById(rol.getIdRol());

        if(rolOptional.isEmpty())
            throw new NotFoundException("Rol no encontrado");

        long usuariosByRol = rolRepository.countUsuariosByRolId(rol.getIdRol());
        if(usuariosByRol > 0)
            throw new BadRequestException("No se puede borrar un rol que tiene usuarios");

        Rol rolToUpdate = rolOptional.get();

//        // Se guarda en la bitacora
//        bitacoraRolService.guardarBitacora("Eliminar", rolToUpdate, rol, rol.getBorradoPor());

        // Se actualiza y cambia estatatus
        rolToUpdate.setBorrado(true);
        rolToUpdate.setFechaBorrado(new Date());
        rolToUpdate.setBorradoPor(rol.getBorradoPor());
        rolRepository.save(rolToUpdate);
    }

    @Override
    @Transactional
    public void update(Rol rol) {
        Integer ID_ROL_OWNER = 1;
        if(Objects.equals(rol.getIdRol(), ID_ROL_OWNER))
            throw new BadRequestException("No es posible actualizar el rol propietario.");

        Optional<Rol> rolOptional = rolRepository.findById(rol.getIdRol());

        if(rolOptional.isEmpty())
            throw new NotFoundException("Rol no encontrado");

        if(rol.getNombre() != null && !rol.getNombre().isEmpty()) {
            // Se agrega validación si ya existe un rol con el mismo nombre
            boolean existsByNombre = rolRepository.existsByNombreAndIdRolNot(rol.getNombre(), rol.getIdRol());
            if(existsByNombre)
                throw new BadRequestException("Ya existe un rol con el nombre: " + rol.getNombre());

            // Se agrega el código de acuerdo al nombre
            String codigoRol = "ROLE_" + rol.getNombre().replace(" ", "_").toUpperCase();
            rol.setCodigo(codigoRol);
        }

        Rol rolToUpdate = rolOptional.get();
//
//        // Se guarda en la bitacora
//        bitacoraRolService.guardarBitacora("Actualizar", rolToUpdate, rol, rol.getModificadoPor());

        // Se actualizan los datos
        rolToUpdate.setCodigo(rol.getCodigo());
        rolToUpdate.setNombre(rol.getNombre());
        rolToUpdate.setDescripcion(rol.getDescripcion());
        rolToUpdate.setModificadoPor(rol.getModificadoPor());

        rolRepository.save(rolToUpdate);
    }

}
