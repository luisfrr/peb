package gob.yucatan.security.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuario", schema = "seguridad")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@SQLRestriction("borrado = false")
public class Usuario implements UserDetails {

    @Id
    @Column(name = "id")
    private Integer idUsuario;

    @Column(name = "usuario", nullable = false)
    private String usuario;

    @Column(name = "contrasenia", nullable = false)
    private String contrasenia;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "correo", nullable = false)
    private String email;

    @Column(name = "correo_confirmado")
    private Boolean correoConfirmado;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="usuario", fetch = FetchType.EAGER)
    private Set<UsuarioRol> usuarioRolSet;

    @Column(name = "estatus", nullable = false)
    private Integer estatus;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado;

    @Column(name = "creado_en", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "creado_por", nullable = false, updatable = false)
    private String creadoPor;

    @Column(name = "modificado_en", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    @Column(name = "modificado_por", insertable = false)
    private String modificadoPor;

    @Column(name = "borrado_en", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaBorrado;

    @Column(name = "borrado_por", insertable = false)
    private String borradoPor;

    //region Transients

    @Transient
    private boolean rolOwner;

    @Transient
    private List<Integer> idRolList;

    @Transient
    @Getter(AccessLevel.NONE)
    private Collection<? extends GrantedAuthority> authorities;

    //endregion Transients

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.contrasenia;
    }

    @Override
    public String getUsername() {
        return this.usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.estatus != null && this.estatus == 1;
    }

    public String getRoles() {
        String roles = Strings.EMPTY;
        if(this.usuarioRolSet != null && !this.usuarioRolSet.isEmpty()) {
            roles = this.usuarioRolSet.stream()
                    .map(usuarioRol -> usuarioRol.getRol().getNombre())
                    .collect(Collectors.joining(", "));
        }
        return roles;
    }

    public Usuario(Usuario original) {
        this.idUsuario = original.getIdUsuario();
        this.usuario = original.getUsuario();
        this.contrasenia = original.getContrasenia();
        this.nombre = original.getNombre();
        this.email = original.getEmail();
        this.correoConfirmado = original.getCorreoConfirmado();
        this.usuarioRolSet  = original.getUsuarioRolSet();
        this.estatus = original.getEstatus();
        this.fechaCreacion = original.getFechaCreacion();
        this.creadoPor  = original.getCreadoPor();
        this.fechaModificacion = original.getFechaModificacion();
        this.modificadoPor  = original.getModificadoPor();
        this.fechaBorrado = original.getFechaBorrado();
        this.borradoPor  = original.getBorradoPor();
        this.rolOwner = original.isRolOwner();
        this.idRolList = original.getIdRolList();
        this.authorities = original.getAuthorities();
    }
}
