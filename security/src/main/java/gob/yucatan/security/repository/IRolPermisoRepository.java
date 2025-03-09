package gob.yucatan.security.repository;


import gob.yucatan.security.entity.Permiso;
import gob.yucatan.security.entity.Rol;
import gob.yucatan.security.entity.RolPermiso;
import gob.yucatan.security.enums.EstatusPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface IRolPermisoRepository extends JpaRepository<RolPermiso, Long>, JpaSpecificationExecutor<RolPermiso> {

    @Query("select r.permiso from RolPermiso r where r.rol in ?1 and r.estatusPermiso = ?2")
    List<Permiso> findPermisoByRolInAndEstatusPermiso(Collection<Rol> rols, EstatusPermiso estatusPermiso);

}
