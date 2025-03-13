package gob.yucatan.peb.security.service.iface;

import gob.yucatan.peb.security.entity.Permiso;
import gob.yucatan.peb.security.entity.Rol;
import gob.yucatan.peb.security.entity.RolPermiso;

import java.util.List;

public interface IRolPermisoService {
    List<RolPermiso> findAllDynamic(RolPermiso rolPermiso);
    List<RolPermiso> findByRol(Rol rol, Permiso permiso);
    void asignarPermiso(RolPermiso rolPermiso, String userName);
}
