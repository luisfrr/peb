package gob.yucatan.peb.security.service.iface;

import gob.yucatan.peb.security.entity.Permiso;
import gob.yucatan.peb.security.entity.Usuario;
import gob.yucatan.peb.security.entity.UsuarioPermiso;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface IUsuarioPermisoService {
    List<UsuarioPermiso> findAllDynamic(UsuarioPermiso usuarioPermiso);
    List<UsuarioPermiso> findByUsuario(Usuario usuario, Permiso permisoFilter);
    void asignarPermiso(UsuarioPermiso usuarioPermiso, String userName);
    List<? extends GrantedAuthority> getAuthorities(Integer idUsuario);
}
