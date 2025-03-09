package gob.yucatan.security.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rol", schema = "seguridad")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@SQLRestriction("borrado = false")
public class Rol {

    @Id
    @Column(name = "id")
    private Integer idRol;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "rol")
    private Set<UsuarioRol> usuarioRolSet;

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

    @Transient
    private boolean leftJoinUsuarioRolSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rol rol = (Rol) o;
        return Objects.equals(idRol, rol.idRol);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idRol);
    }

}
