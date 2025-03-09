package gob.yucatan.security.enums;

import lombok.Getter;

@Getter
public enum EstatusPermiso {

    NO_ASIGNADO("No asignado"),
    HABILITADO("Habilitado"),
    DESHABILITADO("Deshabilitado");

    private final String label;

    EstatusPermiso(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

}
