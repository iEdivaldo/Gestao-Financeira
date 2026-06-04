import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule, FormBuilder,
  FormGroup, Validators, AbstractControl, ValidationErrors
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, RouterLink,
    MatCardModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatProgressSpinnerModule, MatSnackBarModule
  ],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss']
})
export class RegistroComponent {

  carregando = signal(false);
  senhaVisivel = signal(false);
  confirmaSenhaVisivel = signal(false);
  erroRegistro = signal<string | null>(null);

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmaSenha: ['', Validators.required]
    }, {
      // Validador customizado no nível do form — compara os dois campos de senha
      validators: this.senhasIguaisValidator
    });
  }

  // Validador customizado — retorna erro se as senhas forem diferentes
  private senhasIguaisValidator(form: AbstractControl): ValidationErrors | null {
    const senha = form.get('senha')?.value;
    const confirma = form.get('confirmaSenha')?.value;

    // Só valida se ambos os campos tiverem valor
    if (senha && confirma && senha !== confirma) {
      return { senhasDiferentes: true }; // adiciona o erro ao form inteiro
    }
    return null; // sem erro
  }

  get nomeCtrl() { return this.form.get('nome')!; }
  get emailCtrl() { return this.form.get('email')!; }
  get senhaCtrl() { return this.form.get('senha')!; }
  get confirmaSenhaCtrl() { return this.form.get('confirmaSenha')!; }

  toggleSenhaVisivel(): void {
    this.senhaVisivel.update(v => !v);
  }

  toggleConfirmaSenhaVisivel(): void {
    this.confirmaSenhaVisivel.update(v => !v);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.carregando.set(true);
    this.erroRegistro.set(null);

    this.authService.registro({
      nome: this.nomeCtrl.value,
      email: this.emailCtrl.value,
      senha: this.senhaCtrl.value,
      perfil: 'USUARIO' // perfil padrão para auto-registro
    }).subscribe({
      next: () => {
        // Exibe mensagem de sucesso com MatSnackBar
        this.snackBar.open(
          'Conta criada com sucesso! Faça login para continuar.',
          'OK',
          { duration: 5000, panelClass: 'snack-sucesso' }
        );
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.erroRegistro.set(
          err.error?.mensagem || 'Erro ao criar conta. Tente novamente.'
        );
        this.carregando.set(false);
      }
    });
  }
}