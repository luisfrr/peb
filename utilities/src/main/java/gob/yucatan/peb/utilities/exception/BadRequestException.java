package gob.yucatan.peb.utilities.exception;

/**
 * Excepción lanzada cuando un dato de entrada no cumple con las reglas de validación.
 */
public class BadRequestException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje de error.
     *
     * @param message Mensaje descriptivo del error.
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje de error y la causa del error
     *
     * @param message Mensaje descriptivo del error.
     * @param throwable La causa original del error (puede ser otra excepción).
     */
    public BadRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
