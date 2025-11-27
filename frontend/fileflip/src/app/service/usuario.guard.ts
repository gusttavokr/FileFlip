import { inject } from "@angular/core";
import { CanActivateFn } from "@angular/router";
import { UsuarioService } from "./usuario";
import { Router } from "@angular/router";

export const guardAuth : CanActivateFn = () => {
    return inject(UsuarioService).isAuthenticated() ? true : inject (Router).createUrlTree(['/login']);
}