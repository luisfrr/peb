package gob.yucatan.peb.security.service.iface;

import gob.yucatan.peb.security.entity.Permiso;

import java.util.List;

public interface IPermisoService {
    List<Permiso> findAllDynamic(Permiso permiso);
    List<Permiso> findAll();
    void updateAll(List<Permiso> permisos);
}
