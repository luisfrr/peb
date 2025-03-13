package gob.yucatan.peb.security.entity;

import gob.yucatan.peb.security.enums.TipoPermiso;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permiso", schema = "seguridad")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@SQLRestriction("borrado = false")
public class Permiso {

    @Id
    @Column(name = "id")
    private Integer idPermiso;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Permiso permisoParent;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "permisoParent")
    private Set<Permiso> subPermisos;

    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TipoPermiso tipoPermiso;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "clase")
    private String className;

    @Column(name = "metodo")
    private String methodName;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permiso permiso = (Permiso) o;
        return Objects.equals(idPermiso, permiso.idPermiso) && Objects.equals(codigo, permiso.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPermiso, codigo);
    }

    @PrePersist
    public void prePersist() {
        borrado = Boolean.FALSE;
        fechaCreacion = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        fechaModificacion = new Date();
    }

}
