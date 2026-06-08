import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../core/services/auth.service';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule, RouterLink,
    MatToolbarModule, MatIconModule,
    MatButtonModule, MatMenuModule, MatDividerModule
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  // input() é a forma moderna do Angular 17+ para receber dados do pai
  // Substitui o @Input()
  sidebarAberta = input<boolean>(true);

  // output() para emitir eventos — substitui @Output() EventEmitter
  toggleSidebar = output<void>();

  constructor(readonly authService: AuthService) {}
}