package gob.yucatan.security.repository;

import gob.yucatan.security.entity.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IUsuarioRolRepository extends JpaRepository<UsuarioRol, Long>, JpaSpecificationExecutor<UsuarioRol> {
}
