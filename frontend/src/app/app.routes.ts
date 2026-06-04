import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  // Rota padrão — redireciona para login
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // Rotas públicas — carregamento lazy com loadComponent
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component')
        .then(m => m.LoginComponent)
  },
  {
    path: 'registro',
    loadComponent: () =>
      import('./features/auth/registro/registro.component')
        .then(m => m.RegistroComponent)
  },

  // Rotas protegidas — exigem login (authGuard)
  // O layout principal será adicionado nas próximas etapas
//   {
//     path: 'dashboard',
//     canActivate: [authGuard],
//     loadComponent: () =>
//       import('./features/dashboard/dashboard.component')
//         .then(m => m.DashboardComponent)
//     // Componente será criado na Etapa 7
//   },

  // Rota curinga — qualquer rota inválida vai para login
  { path: '**', redirectTo: '/login' }
];