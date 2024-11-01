package com.dev.loja.service;

import com.dev.loja.exception.EntityNotFoundException;
import com.dev.loja.model.Lancamento;
import com.dev.loja.model.Produto;
import com.dev.loja.repository.LancamentoRepository;
import com.dev.loja.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LancamentoService {
    private LancamentoRepository lancamentoRepository;
    private ProdutoRepository produtoRepository;

    @CacheEvict(cacheNames = {"produtos", "produtos-vitrine"}, allEntries = true)
    public ResponseEntity<?> lancarProduto(Lancamento lancamento, UserDetails userDetails) {
        var produto = buscarProdutoPorId(lancamento.getProduto().getId());

        if(lancamento.getQuantidade() > 0 ){
            if(lancamento.getQuantidade() > 100)
                throw  new RuntimeException("Essa operação está limitada a 100 produtos");

            List<Lancamento> lancamentos = new ArrayList<>();

            for(int i=0; i < lancamento.getQuantidade(); i++){
                Lancamento lanc = new Lancamento();
                lanc.setProduto(lancamento.getProduto());
                lanc.setDataLancamento(LocalDateTime.now());
                lanc.setCodigoBarras(lancamento.getCodigoBarras());
                lanc.setResponsavel(userDetails.getUsername());
                lancamentos.add(lanc);
                produto.setEstoqueAtual(produto.getEstoqueAtual() + 1);
            }
            lancamentoRepository.saveAll(lancamentos);
            produtoRepository.save(produto);

        }
        return new ResponseEntity<>(produto, HttpStatus.CREATED);
    }

    public ResponseEntity<?> consultarEstoque(Long produtoId) {
        var produto = buscarProdutoPorId(produtoId);
        return new ResponseEntity<>(lancamentoRepository.consultarEstoque(produtoId), HttpStatus.OK);
    }

    private Produto buscarProdutoPorId(Long id) {
        var busca = produtoRepository.findById(id);
        if (busca.isEmpty())
            throw new EntityNotFoundException("Produto não encontrado");

        return busca.get();
    }

}
