package gob.yucatan.utilities.exception;

/**
 * Excepción genérica para errores internos inesperados en la aplicación.
 */
public class InternalErrorException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje de error.
     *
     * @param message Mensaje descriptivo del error.
     */
    public InternalErrorException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje de error y la causa del error
     *
     * @param message Mensaje descriptivo del error.
     * @param throwable La causa original del error (puede ser otra excepción).
     */
    public InternalErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
