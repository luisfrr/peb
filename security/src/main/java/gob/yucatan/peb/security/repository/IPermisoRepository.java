package gob.yucatan.peb.security.repository;

import gob.yucatan.peb.security.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IPermisoRepository extends JpaRepository<Permiso, Integer>, JpaSpecificationExecutor<Permiso> {
}
