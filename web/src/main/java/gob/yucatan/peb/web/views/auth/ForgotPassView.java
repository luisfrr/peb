package gob.yucatan.peb.web.views.auth;

import gob.yucatan.peb.security.entity.Usuario;
import gob.yucatan.peb.security.service.iface.IUsuarioService;
import gob.yucatan.peb.utilities.exception.BadRequestException;
import gob.yucatan.peb.web.views.beans.Messages;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope("view")
@RequiredArgsConstructor
@Slf4j
public class ForgotPassView implements Serializable {

    private @Getter String title;
    private @Getter @Setter String email;
    private @Getter boolean showReturnToLogin = false;
    private @Getter boolean showFormPassword = false;

    private final IUsuarioService usuarioService;
//    private final IEmailService emailService;

    @PostConstruct
    public void init() {
        log.info("ForgotPassView init");
        title = "¿Olvidaste tu contraseña?";

        showReturnToLogin = false;
        showFormPassword = true;

    }

    public void forgotPassword() {
        try {
            if(email == null)
                throw new BadRequestException("Correo electrónico es un campo obligatorios.");
            Usuario usuario = usuarioService.olvidasteTuPassword(this.email);
            showFormPassword = false;
            showReturnToLogin = true;
//            emailService.sendForgotPasswordEmail(usuario);
        } catch (Exception e) {
            Messages.addError(e.getMessage());
        }
    }

    public String returnToLogin() {
        SecurityContextHolder.clearContext();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login?faces-redirect=true";
    }
}
