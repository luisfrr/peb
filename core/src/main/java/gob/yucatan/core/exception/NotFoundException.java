package gob.yucatan.core.exception;

/**
 * Excepción lanzada cuando una entidad no es encontrada en la base de datos.
 * Se usa típicamente para manejar errores 404 en la aplicación.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje de error.
     *
     * @param message Mensaje descriptivo del error.
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje de error y la causa del error
     *
     * @param message Mensaje descriptivo del error.
     * @param throwable La causa original del error (puede ser otra excepción).
     */
    public NotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
