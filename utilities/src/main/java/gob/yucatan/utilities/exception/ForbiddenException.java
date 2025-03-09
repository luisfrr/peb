package gob.yucatan.utilities.exception;

/**
 * Excepción lanzada cuando un usuario intenta realizar una operación
 * para la que no tiene permisos adecuados.
 */
public class ForbiddenException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje de error.
     *
     * @param message Mensaje descriptivo del error.
     */
    public ForbiddenException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje de error y la causa del error
     *
     * @param message Mensaje descriptivo del error.
     * @param throwable La causa original del error (puede ser otra excepción).
     */
    public ForbiddenException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
