package gob.yucatan.peb.security.repository;

import gob.yucatan.peb.security.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {

    @Query("select u from Usuario u left join fetch u.usuarioRolSet where upper(u.usuario) = upper(?1)")
    Optional<Usuario> findByUsuarioIgnoreCase(String usuario);

    @Query("select u from Usuario u where upper(u.email) = upper(?1)")
    Optional<Usuario> findByEmailIgnoreCase(String email);

    @Query("""
            select (count(u) > 0) from Usuario u
            where u.estatus <> ?1 and upper(u.usuario) = upper(?2)""")
    boolean existsByEstatusNotAndUsuario(Integer estatus, String usuario);

    @Query("""
            select (count(u) > 0) from Usuario u
            where u.estatus <> ?1 and upper(u.usuario) = upper(?2)""")
    boolean existsByEstatusNotAndEmail(Integer estatus, String email);

    @Query("""
            select (count(u) > 0) from Usuario u
            where u.estatus <> ?1 and upper(u.usuario) = upper(?2) and u.idUsuario <> ?3""")
    boolean existsByEstatusNotAndUsuario(Integer estatus, String usuario, Long idUsuario);

    @Query("""
            select (count(u) > 0) from Usuario u
            where u.estatus <> ?1 and upper(u.usuario) = upper(?2) and u.idUsuario <> ?3""")
    boolean existsByEstatusNotAndEmail(Integer estatus, String email, Long idUsuario);

}
