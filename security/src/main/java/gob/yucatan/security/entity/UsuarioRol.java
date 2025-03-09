package gob.yucatan.security.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_rol", schema = "seguridad")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioRol {

    @Id
    @Column(name = "id")
    private Long idUsuarioRol;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

}
