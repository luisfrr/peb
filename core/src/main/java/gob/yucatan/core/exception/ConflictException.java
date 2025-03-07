package gob.yucatan.core.exception;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe en el sistema.
 */
public class ConflictException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje de error.
     *
     * @param message Mensaje descriptivo del error.
     */
    public ConflictException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje de error y la causa del error
     *
     * @param message Mensaje descriptivo del error.
     * @param throwable La causa original del error (puede ser otra excepción).
     */
    public ConflictException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
