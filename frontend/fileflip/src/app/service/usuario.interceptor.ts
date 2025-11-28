import { HttpEvent, HttpHandlerFn, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { Observable } from "rxjs";
import { UsuarioService } from "./usuario";
import { Router } from "@angular/router";

export function autenticacaoInterceptor(
    request: HttpRequest<unknown>, next: HttpHandlerFn
) : Observable<HttpEvent<unknown>> {

    if (request.url.includes('/auth/login') || request.url.includes('/auth/cadastro')) {
        return next(request);
    }
    const userService = inject(UsuarioService);

    // Se NÃO está autenticado OU o token está vencido, redireciona para login
    if (!userService.isAuthenticated() || userService.tokenVencido()) {
        const route = inject(Router);
        route.navigate(['/login']);
        return new Observable<HttpEvent<unknown>>((Observer) => {
            Observer.complete();
        });
    }

    const token = sessionStorage.getItem('token');

    if (token) {
        const clonedRequest = request.clone({
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        });
        return next(clonedRequest);
    }
    return next(request);
}