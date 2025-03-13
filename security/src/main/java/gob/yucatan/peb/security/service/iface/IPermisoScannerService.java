package gob.yucatan.peb.security.service.iface;

import gob.yucatan.peb.security.entity.Permiso;

import java.util.List;

public interface IPermisoScannerService {
    List<Permiso> getPermisos(String scanPackage, String userName);
}
