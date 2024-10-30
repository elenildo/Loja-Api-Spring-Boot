package com.dev.loja.service;

import com.dev.loja.dto.ImagemDtoSaida;
import com.dev.loja.dto.ProdutoDtoEntrada;
import com.dev.loja.dto.ProdutoDtoSaida;
import com.dev.loja.dto.ProdutoDtoVitrine;
import com.dev.loja.exception.EntityNotFoundException;
import com.dev.loja.exception.FileOperationException;
import com.dev.loja.model.Imagem;
import com.dev.loja.model.Produto;
import com.dev.loja.repository.CategoriaRepository;
import com.dev.loja.repository.ImagemRepository;
import com.dev.loja.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.io.IOUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProdutoService {
    private ProdutoRepository produtoRepository;
    private CategoriaRepository categoriaRepository;
    private ImagemRepository imagemRepository;
    private BeanUtilsBean beanUtilsBean;

//    private final String path = System.getProperty("user.dir") + "/upload/produtos/";
    private final String path = "/home/apps/loja/imagens/produtos/";

    @Cacheable("produtos")
    public Page<ProdutoDtoSaida> listarTudo(Pageable pageable) {
        Page<Produto> prods = produtoRepository.findAll(pageable);
        simulateLatency(1000);
        return new PageImpl<>(prods.stream().map(
                produto -> {
                    produto.setImagens(this.carregarImagemPorProduto(produto));
                    return new ProdutoDtoSaida(produto);
                }
        ).toList(), pageable, prods.getTotalElements());
    }

    @Cacheable("produtos-vitrine")
    public Page<ProdutoDtoVitrine> listarTudoVitrine(Pageable pageable) {
        Page<Produto> prods = produtoRepository.findAll(pageable);
        return new PageImpl<>(prods.stream().map(
                produto -> {
                    produto.setImagens(this.carregarImagemPorProduto(produto));
                    return new ProdutoDtoVitrine(produto);
                }).toList(), pageable, prods.getTotalElements());
    }

    public Page<ProdutoDtoVitrine> produtosPorCategoriaNome(String categoria, Pageable pageable) {
        Page<Produto> prods = produtoRepository.getProdutoByCategoriaNome(categoria, pageable);
        return new PageImpl<>(prods.stream().map(
                produto -> {
                    produto.setImagens(this.carregarImagemPorProduto(produto));
                    return new ProdutoDtoVitrine(produto);
                }).toList(), pageable, prods.getTotalElements());
    }

    @CacheEvict(cacheNames = {"produtos", "produtos-vitrine"}, allEntries = true)
    public Produto novo(ProdutoDtoEntrada produtoDto) {
        var busca = categoriaRepository.findById(produtoDto.categoria.getId());
        if(busca.isEmpty())
//            return new ResponseEntity<>("A categoria informada não existe", HttpStatus.BAD_REQUEST);
            throw new EntityNotFoundException("A categoria informada não existe");

        return new Produto(produtoDto);
    }

    public ProdutoDtoSaida buscarPorId(Long id) {
        var produto = buscarProdutoPorId(id);
        return new ProdutoDtoSaida(produto);
    }

    public ProdutoDtoVitrine buscarPorIdHome(Long id) { //home
        var produto = buscarProdutoPorId(id);
        produto.setImagens(this.carregarImagemPorProduto(produto));
        return new ProdutoDtoVitrine(produto);
    }

    public ProdutoDtoSaida adicionarImagens(Long id, MultipartFile[] files) {
        List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");
        for(MultipartFile file : files){
            if(!contentTypes.contains(file.getContentType()))
                throw new FileOperationException("Somente imagens PNG, JPG e GIF são permitidas.");
        }
        var produto = buscarProdutoPorId(id);
        String filename;
        List<Imagem> imagens = produto.getImagens();
        Path fileNameAndPath;

        for(MultipartFile file : files){
            filename = UUID.randomUUID().toString();
//            filename = file.getOriginalFilename();
            fileNameAndPath = Paths.get(this.path, filename);
            Imagem imagem = new Imagem();
            imagem.setCaminho(filename);
            imagem.setProduto(produto);
            imagens.add(imagem);
            try {
                Files.write(fileNameAndPath, file.getBytes());;
            } catch (IOException e) {
                throw new FileOperationException("Erro ao fazer upload: "+e.getMessage());
            }
        }

        produto.setImagens(imagemRepository.saveAll(imagens));
        produtoRepository.save(produto);

        return new ProdutoDtoSaida(produto);
    }

    public ProdutoDtoSaida removerImagens(Long produtoId, List<ImagemDtoSaida> imagens) {
        Imagem imagem;
        File arquivo;
        for(ImagemDtoSaida imagemDto : imagens){
            imagem = imagemRepository.findByCaminho(imagemDto.caminho);
            if(imagem != null){
                imagemRepository.delete(imagem);
                arquivo = new File(path + imagemDto.caminho);
                if(! arquivo.delete())
                    throw new FileOperationException("Não foi possível encontrar a imagem. " +
                            "Ela pode ter sido renomeada ou removida");
            }
        }
        return buscarPorId(produtoId);
    }

    public Page<ProdutoDtoSaida> buscarPorNome(String busca, Pageable pageable) {
        Page<Produto> prods = produtoRepository.findByNomeContainingIgnoreCase(busca, pageable);
        return new PageImpl<>(prods.stream().map(
                produto -> {
                    produto.setImagens(this.carregarImagemPorProduto(produto));
                    return new ProdutoDtoSaida(produto);
                }
        ).toList(), pageable, prods.getTotalElements());
    }

    public ProdutoDtoSaida editar(ProdutoDtoEntrada produto, Long id) throws InvocationTargetException, IllegalAccessException {
        var prod = buscarProdutoPorId(id);
        beanUtilsBean.copyProperties(prod, new Produto(produto));

        return new ProdutoDtoSaida(produtoRepository.save(prod));
    }

    private Produto buscarProdutoPorId(Long id) {
        var busca = produtoRepository.findById(id);
        if (busca.isEmpty())
            throw new EntityNotFoundException("Produto não encontrado");

        return busca.get();
    }

    private List<Imagem> carregarImagemPorProduto(Produto produto){
        produto.getImagens().forEach(imagem -> {
            try {
                InputStream inputStream = new FileInputStream(this.path+imagem.getCaminho());
                imagem.setArquivo(IOUtils.toByteArray(inputStream));
            } catch (FileNotFoundException e) {
                throw new FileOperationException("Erro ao buscar arquivo de imagem. "+e.getMessage());
            } catch (IOException e) {
                throw new FileOperationException("Erro ao converter arquivo. "+e.getMessage());
            }
        });
        return produto.getImagens();
    }

    private void simulateLatency(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
