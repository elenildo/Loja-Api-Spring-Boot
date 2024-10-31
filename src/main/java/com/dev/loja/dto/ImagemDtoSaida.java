package com.dev.loja.dto;

import com.dev.loja.model.Imagem;

import java.io.Serializable;
import java.util.List;

public class ImagemDtoSaida implements Serializable {
    public boolean delete = false;
    public String caminho;
    public byte[] imagem;

    public ImagemDtoSaida(Imagem imagem){
        this.caminho = imagem.getCaminho();
        this.imagem = imagem.getArquivo();
    }
    public ImagemDtoSaida(){}
}
