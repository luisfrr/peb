package gob.yucatan.security.repository;

import gob.yucatan.security.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IPermisoRepository extends JpaRepository<Permiso, Integer>, JpaSpecificationExecutor<Permiso> {
}
