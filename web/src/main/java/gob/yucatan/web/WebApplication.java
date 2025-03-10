package gob.yucatan.web;

import gob.yucatan.security.entity.Permiso;
import gob.yucatan.security.service.iface.IPermisoScannerService;
import gob.yucatan.security.service.iface.IPermisoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {"gob.yucatan"})
@RequiredArgsConstructor
@Slf4j
public class WebApplication implements CommandLineRunner {

    private final IPermisoService permisoService;
    private final IPermisoScannerService permisoScannerService;

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    public void run(String... args) {
        setTimeZone();
        loadPermisos();
    }

    private void setTimeZone() {
        // Establecer la zona horaria por defecto
        TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));
    }

    private void loadPermisos() {
        try {
            List<Permiso> permisos = permisoScannerService.getPermisos("gob.yucatan.web",
                    "owner");
            permisoService.updateAll(permisos);
            log.info("Configuración de permisos actualizado correctamente");
        } catch (Exception ex) {
            log.error("No se ha logrado actualizar la configuración de permisos", ex);
        }
    }
}
