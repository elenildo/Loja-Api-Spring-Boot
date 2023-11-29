package com.dev.loja.beans;

import com.dev.loja.dto.EmailResponseDto;
import com.dev.loja.model.ItemPedido;
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
        String page = generateOrderText(pedido);

        email.emailFrom = "loja@teste.com";
        email.emailTo = to;
        email.subject = "Pedido "+pedido.getNumero();
        email.ownerRef = "loja";
        email.text = page;

        headers.set("Content-type", "application/json;charset=UTF-8");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<EmailResponseDto> request = new HttpEntity<>(email, headers);
        restTemplate.postForEntity("http://localhost:8090/sending-email", request, EmailResponseDto.class);
    }
    private String generateOrderText(Pedido pedido){
        StringBuilder ped = new StringBuilder(String.format("<h3>Pedido %d</h3>", pedido.getNumero()));
        ped.append(String.format("<p>Forma de pagamento: %s</p>", pedido.getFormaPagamento()));
        ped.append(String.format("<p>Cliente: %s %s</p>",pedido.getUser().getNome(), pedido.getUser().getSobrenome()));
        ped.append("<h4>Itens</h4>");
        ped.append("<table style='text-align:left'><thead><th>Produto</th><th>Preço Un.</th><th>Qtd</th><th>Subtotal</th></thead>");
        for(ItemPedido item : pedido.getItens()){
            ped.append(String.format("<tr><td>%s</td>", item.getProduto().getNome()))
                    .append(String.format("<td>%s</td>", item.getProduto().getPrecoVenda()))
                    .append(String.format("<td>%s</td>", item.getQuantidade()))
                    .append(String.format("<td>%s<td></tr>", item.getSubtotal()));
        }
        ped.append("</table>")
        .append(String.format("<p>Subtotal: R$ %s</p>",pedido.getSubtotal()))
        .append(String.format("<p>Frete: R$ %s</p>",pedido.getFrete()))
        .append(String.format("<p>Descontos: R$ %s</p>",pedido.getDescontos()))
        .append(String.format("<p>Total: R$ %s</p>",pedido.getTotal()))
        .append("<center><h3>Essa é uma mensagem automática, não responder</h3></center>");

        return ped.toString();

    }
}
