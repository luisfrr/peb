package gob.yucatan.security.enums;

import lombok.Getter;

@Getter
public enum TipoPermiso {

    CONTROLADOR("Controlador"),
    ESCRITURA("Escritura"),
    LECTURA("Lectura");

    private final String label;

    TipoPermiso(String label) {
        this.label = label;
    }

}
