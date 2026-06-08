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
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./shared/layout/layout.component').then(m => m.LayoutComponent),
    // children: rotas filhas renderizam dentro do <router-outlet> do Layout
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      // As demais rotas (transacoes, contas, etc.) serão adicionadas nas próximas etapas
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  // O layout principal será adicionado nas próximas etapas


  // Rota curinga — qualquer rota inválida vai para login
  { path: '**', redirectTo: '/login' }
];