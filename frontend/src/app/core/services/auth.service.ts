import { computed, Injectable, signal } from "@angular/core";
import { LoginRequest, LoginResponse, Perfil, UsuarioLogado, UsuarioRequest, UsuarioResponse } from "../models/auth.models";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { Observable, tap } from "rxjs";

const TOKEN_KEY = 'fincontrol_token';
const USUARIO_KEY = 'fincontrol_usuario';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    // Signal para armazenar o usuário logado, inicializado com o valor do localStorage
    // signal() é usado para criar um estado reativo que pode ser atualizado e observado em toda a aplicação
    private _usuarioLogado = signal<UsuarioLogado | null>(this.carregarUsuarioStorage());

    // computed() é usado para criar uma propriedade derivada que recalcula seu valor sempre que o sinal de origem (_usuarioLogado) for atualizado
    readonly usuarioLogado = computed(() => this._usuarioLogado());

    readonly estaLogado = computed(() => this._usuarioLogado() !== null);

    readonly perfil = computed(() => this._usuarioLogado()?.perfil ?? null);

    constructor(private http: HttpClient, private router: Router) { }

    // login
    login(dados: LoginRequest): Observable<LoginResponse> {
        return this.http.post<LoginResponse>('/api/v1/auth/login', dados).pipe(
            tap(response => {
                localStorage.setItem(TOKEN_KEY, response.token);
                
                const usuarioLogado: UsuarioLogado = {
                    id: response.id,
                    nome: response.nome,
                    email: response.email,
                    perfil: response.perfil
                };
                localStorage.setItem(USUARIO_KEY, JSON.stringify(usuarioLogado));
                this._usuarioLogado.set(usuarioLogado);
            })
        )
    }

    // registro
    registro(dados: UsuarioRequest): Observable<UsuarioResponse> {
        return this.http.post<UsuarioResponse>('/api/v1/auth/registro', dados);
    }

    // logout
    logout(): void {
        // notifica o backend para invalidar o token (opcional, dependendo da implementação do backend)
        this.http.post('/api/v1/auth/logout', {}).subscribe({ error: () => {} });

        // remover os dados do localStorage
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USUARIO_KEY);

        // limpa o signal
        this._usuarioLogado.set(null);

        // redireciona para a página de login
        this.router.navigate(['/login']);
    }

    // getters
    getToken(): string | null {
        return localStorage.getItem(TOKEN_KEY);
    }

    // verifica se usuario tem perfil admin
    temPerfilAdmin(...perfis: Perfil[]): boolean {
        const perfilAtual = this.perfil();
        if (!perfilAtual) return false;
        return perfis.includes(perfilAtual);
    }

    // carrega usuario do localStorage
    private carregarUsuarioStorage(): UsuarioLogado | null {
        try {
            const dados = localStorage.getItem(USUARIO_KEY);
            if (!dados || !localStorage.getItem(TOKEN_KEY)) return null; // se não tiver token, considera usuário deslogado
            return JSON.parse(dados) as UsuarioLogado;
        } catch {
            localStorage.removeItem(USUARIO_KEY);
            localStorage.removeItem(TOKEN_KEY);
            return null;
        }
    }

}