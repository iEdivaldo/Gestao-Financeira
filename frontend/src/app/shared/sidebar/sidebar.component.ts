import { Component, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatRippleModule } from '@angular/material/core';
import { AuthService } from '../../core/services/auth.service';


// Interface para os itens de navegação da sidebar
interface NavItem {
  label: string;
  icone: string;
  rota: string;
  perfisPermitidos?: string[]; // se vazio, todos os perfis podem ver
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, MatIconModule, MatRippleModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {

  // output() é a forma moderna do Angular 17+ para eventos de saída
  // Substitui o @Output() EventEmitter
  fechar = output<void>();

  // Lista de itens de navegação
  readonly navItems: NavItem[] = [
    { label: 'Dashboard',    icone: 'dashboard',        rota: '/dashboard' },
    { label: 'Transações',   icone: 'swap_horiz',       rota: '/transacoes' },
    { label: 'Contas',       icone: 'account_balance',  rota: '/contas' },
    { label: 'Categorias',   icone: 'label',            rota: '/categorias' },
    { label: 'Orçamentos',   icone: 'savings',          rota: '/orcamentos' },
    { label: 'Relatórios',   icone: 'bar_chart',        rota: '/relatorios' },
  ];

  constructor(readonly authService: AuthService) {}

  onNavClick(): void {
    // Emite o evento de fechar — usado em mobile para fechar a sidebar após navegar
    this.fechar.emit();
  }
}