package br.com.spintec.wstiss.model;

import lombok.Data;

@Data
public class IdentificacaoPrestadorModel {
    private String cnpj;
    private String cpf;
    private String codigoPrestadorNaOperadora;
    private String nomePrestador;
}
