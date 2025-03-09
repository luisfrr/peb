package gob.yucatan.security.service.iface;

import gob.yucatan.security.entity.Permiso;

import java.util.List;

public interface IPermisoScannerService {
    List<Permiso> getPermisos(String scanPackage, String userName);
}
