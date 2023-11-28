package com.dev.loja.beans;

import com.dev.loja.dto.EmailResponseDto;
import com.dev.loja.model.Pedido;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Component
public class EmailSender {

    public void sendOrder(String to, Pedido pedido){
        RestTemplate restTemplate = new RestTemplate();
        EmailResponseDto email= new EmailResponseDto();
        HttpHeaders headers = new HttpHeaders();
        String page = "Pedido: "+pedido.getNumero()+"\n" +
                "Total: R$ "+pedido.getTotal()+"\n";

        email.emailFrom = "loja@teste.com";
        email.emailTo = to;
        email.subject = "Pedido "+pedido.getNumero();
        email.ownerRef = "loja";
        email.text = page;

        headers.set("Content-type", "application/json;charset=UTF-8");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<EmailResponseDto> request = new HttpEntity<>(email, headers);

        try{
            restTemplate.postForEntity("http://localhost:8090/sending-email",request, EmailResponseDto.class);
        }catch (Exception e){
            throw new RuntimeException("Erro ao enviar e-mail. "+e.getMessage());
        }
    }
}
