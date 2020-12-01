package br.com.spintec.wstiss.model;

import br.gov.ans.padroes.tiss.schemas.v30500.DmSimNao;
import lombok.Data;

import java.util.Date;

@Data
public class BeneficiarioModel {
    private String nome;
    private Long numeroCNS;
    private String numeroCarteira;
    private Date validadeCarteira;
    private DmSimNao atendimentoRN;
}