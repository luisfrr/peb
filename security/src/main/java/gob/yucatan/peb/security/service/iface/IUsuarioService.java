package gob.yucatan.peb.security.service.iface;

import gob.yucatan.peb.security.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> findAllDynamic(Usuario usuario);
    Optional<Usuario> findById(Integer id);
    Optional<Usuario> findByUsuario(String usuario);
    Optional<Usuario> findByToken(String token);
    Usuario create(Usuario usuario);
    void delete(Usuario usuario);
    void update(Usuario usuario);
    void activarCuenta(Usuario usuario);
    void deshabilitarCuenta(Integer idUsuario, String userName);
    void habilitarCuenta(Integer idUsuario, String userName);
    Usuario restablecerPassword(Integer idUsuario, String userName);
    Usuario olvidasteTuPassword(String correoElectronico);
    void asignarNuevoPassword(Usuario usuario);
}
