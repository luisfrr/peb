package gob.yucatan.security.service.iface;

import gob.yucatan.security.entity.Permiso;
import gob.yucatan.security.entity.Usuario;
import gob.yucatan.security.entity.UsuarioPermiso;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface IUsuarioPermisoService {
    List<UsuarioPermiso> findAllDynamic(UsuarioPermiso usuarioPermiso);
    List<UsuarioPermiso> findByUsuario(Usuario usuario, Permiso permisoFilter);
    void asignarPermiso(UsuarioPermiso usuarioPermiso, String userName);
    List<? extends GrantedAuthority> getAuthorities(Integer idUsuario);
}
