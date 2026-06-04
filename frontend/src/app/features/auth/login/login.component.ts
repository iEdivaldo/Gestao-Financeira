import { CommonModule } from "@angular/common";
import { Component, signal } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from "../../../core/services/auth.service";

@Component({
    selector: "app-login",
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
    templateUrl: "./login.component.html",
    styleUrl: "./login.component.scss"
})

export class LoginComponent {
    carregando = signal(false);
    senhaVisivel = signal(false);
    erroLogin = signal<string | null>(null);

    form: FormGroup;
    private returnUrl: string;

    constructor(private fb: FormBuilder, private authService: AuthService, private router: Router, private route: ActivatedRoute) {
        // captura a URL de retorno, ou define para dashboard se não houver
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';

        // se o usuário já estiver logado, redireciona para a URL de retorno
        if (this.authService.estaLogado()) {
            this.router.navigate([this.returnUrl]);
        }

        // inicializa o formulário de login com validação
        this.form = this.fb.group({
            email: ['', [Validators.required, Validators.email]],
            senha: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    get emailCtrl() { return this.form.get('email'); }
    get senhaCtrl() { return this.form.get('senha'); }

    toggleSenha(): void {
        this.senhaVisivel.update(visivel => !visivel);
    }

    onSubmit(): void {
        // se o formulário for inválido, marca todos os campos como tocados para exibir mensagens de erro
        if (this.form.invalid) {
            this.form.markAllAsTouched(); // marca todos os campos como tocados para exibir mensagens de erro
            return;
        }

        this.carregando.set(true);
        this.erroLogin.set(null);

        this.authService.login(this.form.value).subscribe({
            next: () => {
                // login bem-sucedido, redireciona para a URL de retorno
                this.router.navigate([this.returnUrl]);
            }, 
            error: (err) => {
                // exibe mensagem de erro personalizada ou genérica
                this.erroLogin.set(err.error?.message || 'Erro ao fazer login. Verifique suas credenciais e tente novamente.');
                this.carregando.set(false);
            }
        })
    }
}