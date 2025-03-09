package gob.yucatan.security.service.iface;

import gob.yucatan.security.entity.Permiso;

import java.util.List;

public interface IPermisoService {
    List<Permiso> findAllDynamic(Permiso permiso);
    List<Permiso> findAll();
    void updateAll(List<Permiso> permisos);
}
