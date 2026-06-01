package com.gestaofinanceira.backend.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.gestaofinanceira.backend.model.Categoria;
import com.gestaofinanceira.backend.model.Usuario;
import com.gestaofinanceira.backend.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // CRUD COMPLETO DE CATEGORIAS POR USUARIOS

    // // GET
    // public List<Categoria> obterCategoriasPorUsuario(Usuario usuario) {
    //     return categoriaRepository.findAllByUsuarioAndAtivo(usuario, true);
    // }

    public Categoria obterCategoriaPorId(Usuario usuario, @NonNull UUID id) {
        return categoriaRepository.findById(id)
                .filter(categoria -> categoria.getUsuario().equals(usuario) && categoria.getAtivo())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    public Categoria criarCategoria(@NonNull Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria atualizarCategoria(@NonNull Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void desativarCategoria(Usuario usuario, @NonNull UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .filter(c -> c.getUsuario().equals(usuario) && c.getAtivo())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        categoria.setAtivo(false);
        categoriaRepository.save(categoria);
    }

    public void ativarCategoria(Usuario usuario, @NonNull UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .filter(c -> c.getUsuario().equals(usuario) && !c.getAtivo())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        categoria.setAtivo(true);
        categoriaRepository.save(categoria);
    }

    public void deletarCategoria(Usuario usuario, @NonNull UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .filter(c -> c.getUsuario().equals(usuario))
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        categoriaRepository.delete(categoria);
    }
}
