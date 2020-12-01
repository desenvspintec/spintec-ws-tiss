package br.com.spintec.wstiss.model;

import br.gov.ans.padroes.tiss.schemas.v30500.*;
import lombok.Data;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;

@Data
public class SolicitacaoProcedimentoModel {
    private String sequencialTransacao;
    private String numeroANS;
    private String numeroGuiaPrestador;
    private IdentificacaoPrestadorModel identificacaoPrestador;
    private BeneficiarioModel beneficiario;
    private CtContratadoProfissionalDados ctContratadoProfissionalDados;
    private List<CtmSpSadtSolicitacaoGuia.ProcedimentosSolicitados> procedimentosSolicitadosList = new ArrayList<>();
    private CtmSpSadtSolicitacaoGuia.DadosExecutante dadosExecutante;
    private XMLGregorianCalendar dataSolicitacao;
    private String caraterAtendimento;
    List<CtmInternacaoSolicitacaoGuia.ProcedimentosSolicitados> solicitacaoGuiaProcedimentosSolicitadosList = new ArrayList<>();
    List<CtmProrrogacaoSolicitacaoGuia.ProcedimentosAdicionais> prorrogacaoSolicitacaoGuiaProcedimentosList = new ArrayList<>();
    List<CtoOdontoSolicitacaoGuia.ProcedimentosSolicitados> odontoSolicitacaoGuiaProcedimentosList = new ArrayList<>();
}
