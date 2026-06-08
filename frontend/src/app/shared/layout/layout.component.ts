import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    MatSidenavModule,
    SidebarComponent,
    HeaderComponent
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {

  // Signal que controla se a sidebar está aberta ou fechada
  sidebarAberta = signal(true);

  // Signal que indica se a tela é mobile (sidebar vira drawer sobreposto)
  isMobile = signal(false);

  constructor(private breakpointObserver: BreakpointObserver) {
    // Observa mudanças no tamanho da tela
    // BreakpointObserver do Angular CDK detecta breakpoints CSS
    this.breakpointObserver.observe([Breakpoints.Handset]).subscribe(result => {
      this.isMobile.set(result.matches);
      // Em mobile, começa com a sidebar fechada
      if (result.matches) {
        this.sidebarAberta.set(false);
      } else {
        this.sidebarAberta.set(true);
      }
    });
  }

  toggleSidebar(): void {
    this.sidebarAberta.update(v => !v);
  }
}