package gob.yucatan.peb.web;

import gob.yucatan.peb.security.entity.Permiso;
import gob.yucatan.peb.security.service.iface.IPermisoScannerService;
import gob.yucatan.peb.security.service.iface.IPermisoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.TimeZone;

@SpringBootApplication()
@ComponentScan(basePackages = "gob.yucatan.peb")
@EnableJpaRepositories("gob.yucatan.peb")
@EntityScan("gob.yucatan.peb")
@RequiredArgsConstructor
@Slf4j
public class WebApplication implements CommandLineRunner {

    private final IPermisoService permisoService;
    private final IPermisoScannerService permisoScannerService;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    public void run(String... args) {
        setTimeZone();
        //loadPermisos();
        log.info("owner: {}", passwordEncoder.encode("12345678"));
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
