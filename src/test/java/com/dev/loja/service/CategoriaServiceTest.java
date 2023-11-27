package com.dev.loja.service;

import com.dev.loja.model.Categoria;
import com.dev.loja.repository.CategoriaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class CategoriaServiceTest {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void listarTudo() {
        assertTrue(true);
    }

    @Test
    void novo() {
    }

    @Test
    @DisplayName("Find one Categoria by Id")
    void buscarPorId() {
//        this.create();
        assertTrue(this.categoriaRepository.findById(1L).isPresent());
    }

    @Test
    void editar() {
    }

    @Test
    void buscarCategoriaPorNome() {
    }

    private void create(){
        Categoria categoria = new Categoria();
        categoria.setNome("categoria-test");
        entityManager.persist(categoria);
    }
}