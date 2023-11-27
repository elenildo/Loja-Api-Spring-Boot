package com.dev.loja.dto;

import com.dev.loja.enums.PedidoStatus;
import jakarta.validation.constraints.NotNull;

public record PedidoDtoEntrada(@NotNull PedidoStatus pedidoStatus) {
}
