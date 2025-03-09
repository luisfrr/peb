package gob.yucatan.security.service.iface;

import gob.yucatan.security.entity.Rol;

import java.util.List;
import java.util.Optional;

public interface IRolService {

    List<Rol> findAllDynamic(Rol rol);
    Optional<Rol> findById(Integer id);
    void save(Rol rol);
    void delete(Rol rol);
    void update(Rol rol);

}
