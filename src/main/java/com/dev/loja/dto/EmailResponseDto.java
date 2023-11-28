package com.dev.loja.dto;


import jakarta.persistence.Lob;

public class EmailResponseDto {
    public String ownerRef;
    public String emailFrom;
    public String emailTo;
    public String subject;
    @Lob
    public String text;
}
