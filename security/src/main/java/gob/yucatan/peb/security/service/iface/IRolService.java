package gob.yucatan.peb.security.service.iface;

import gob.yucatan.peb.security.entity.Rol;

import java.util.List;
import java.util.Optional;

public interface IRolService {

    List<Rol> findAllDynamic(Rol rol);
    Optional<Rol> findById(Integer id);
    void save(Rol rol);
    void delete(Rol rol);
    void update(Rol rol);

}
