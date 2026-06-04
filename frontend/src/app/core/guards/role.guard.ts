import { CanActivateFn, Router } from "@angular/router";
import { Perfil } from "../models/auth.models";
import { AuthService } from "../services/auth.service";
import { inject } from "@angular/core";

export const roleGuard = (...perfisPermitidos: Perfil[]): CanActivateFn => {
    return () => {
        const authService = inject(AuthService);
        const router = inject(Router);

        if (authService.temPerfilAdmin(...perfisPermitidos)) {
            return true;
        }

        router.navigate(['/dashboard']);
        return false;
    }
}