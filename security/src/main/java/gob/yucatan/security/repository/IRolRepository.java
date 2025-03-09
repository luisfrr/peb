package gob.yucatan.security.repository;

import gob.yucatan.security.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IRolRepository extends JpaRepository<Rol, Integer>, JpaSpecificationExecutor<Rol> {

    @Query("select count(distinct usuarioRolSet.idUsuarioRol) from Rol r inner join r.usuarioRolSet usuarioRolSet where usuarioRolSet.rol.idRol = ?1")
    long countUsuariosByRolId(Integer idRol);

    @Query("select (count(r) > 0) from Rol r where r.nombre = ?1")
    boolean existsByNombre(String nombre);

    @Query("select (count(r) > 0) from Rol r where r.nombre = ?1 and r.idRol <> ?2")
    boolean existsByNombreAndIdRolNot(String nombre, Integer idRol);

}
