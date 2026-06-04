export interface LoginRequest {
    email: string;
    senha: string;
}

export interface LoginResponse {
    token: string;
    id: string;
    nome: string;
    email: string;
    perfil: Perfil;
}

export interface UsuarioRequest {
    nome: string;
    email: string;
    senha: string;
    perfil: Perfil;
}

export interface UsuarioResponse {
    id: string;
    nome: string;
    email: string;
    perfil: Perfil;
    ativo: boolean;
    criadoEm: string;
}

export type Perfil = 'ADMIN' | 'GESTOR' | 'USUARIO' | 'VISUALIZADOR';

export interface UsuarioLogado {
    id: string;
    nome: string;
    email: string;
    perfil: Perfil;
}