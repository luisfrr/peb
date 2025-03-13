package gob.yucatan.peb.security.repository;

import gob.yucatan.peb.security.entity.Permiso;
import gob.yucatan.peb.security.entity.Usuario;
import gob.yucatan.peb.security.entity.UsuarioPermiso;
import gob.yucatan.peb.security.enums.EstatusPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IUsuarioPermisoRepository extends JpaRepository<UsuarioPermiso, Long>, JpaSpecificationExecutor<UsuarioPermiso> {

    @Query("select u.permiso from UsuarioPermiso u where u.usuario = ?1 and u.estatusPermiso = ?2")
    List<Permiso> findPermisoByUsuarioAndEstatusPermiso(Usuario usuario, EstatusPermiso estatusPermiso);

}
