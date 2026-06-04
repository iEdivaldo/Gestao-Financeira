import { inject } from "@angular/core"
import { AuthService } from "../services/auth.service"
import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { Router } from "@angular/router";
import { catchError } from "rxjs/internal/operators/catchError";
import { throwError } from "rxjs/internal/observable/throwError";

export const JwtInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    
    const router = inject(Router);

    const token = authService.getToken();

    // se houver token, clona requisição
    const requisicao = token ? req.clone({
        setHeaders: {
            Authorization: `Bearer ${token}`
        }
    }) : req;

    return next(requisicao).pipe(
        catchError((erro: HttpErrorResponse) => {
            if (erro.status === 401) {
                // token expirado ou invalido - faz logout e redireciona para a página de login
                authService.logout();
                router.navigate(['/login']);
            }
            return throwError(() => erro);
        })
    );
}