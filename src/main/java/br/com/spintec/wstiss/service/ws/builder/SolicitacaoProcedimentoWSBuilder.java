package br.com.spintec.wstiss.service.ws.builder;

import br.com.spintec.wstiss.annotation.TissVersion;
import br.com.spintec.wstiss.core.builder.MensagemTissWSBuilder;
import br.com.spintec.wstiss.model.IdentificacaoPrestadorModel;
import br.com.spintec.wstiss.model.SolicitacaoProcedimentoModel;
import br.com.spintec.wstiss.utils.CalculoHash;
import br.com.spintec.wstiss.utils.tiss.CabecalhoTransacaoBuilder;
import br.gov.ans.padroes.tiss.schemas.v30500.*;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@TissVersion(tipoMensagem = SolicitacaoProcedimentoWS.class, versao = "3.05.00")
public class SolicitacaoProcedimentoWSBuilder implements MensagemTissWSBuilder<SolicitacaoProcedimentoWS, SolicitacaoProcedimentoModel> {

    private final CabecalhoTransacaoBuilder cabecalhoBuilder = new CabecalhoTransacaoBuilder();

    @Override
    public SolicitacaoProcedimentoWS builder(SolicitacaoProcedimentoModel solicitacaoProcedimento) {
        final SolicitacaoProcedimentoWS solicitacaoProcedimentoWS = new SolicitacaoProcedimentoWS();

        final CabecalhoTransacao cabecalho = criarCabecalho(solicitacaoProcedimento);
        solicitacaoProcedimentoWS.setCabecalho(cabecalho);

        final CtSolicitacaoProcedimento corpo = criarCorpo(solicitacaoProcedimento);
        solicitacaoProcedimentoWS.setSolicitacaoProcedimento(corpo);

        final String hash = CalculoHash.getHashFromObject(solicitacaoProcedimentoWS);
        solicitacaoProcedimentoWS.setHash(hash);
        return solicitacaoProcedimentoWS;
    }

    private CabecalhoTransacao criarCabecalho(SolicitacaoProcedimentoModel solicitacaoProcedimentoModel) {
        return cabecalhoBuilder.criarCabecalho(DmTipoTransacao.SOLICITACAO_PROCEDIMENTOS, solicitacaoProcedimentoModel.getSequencialTransacao(),
                solicitacaoProcedimentoModel.getNumeroANS(), Optional.ofNullable(solicitacaoProcedimentoModel.getIdentificacaoPrestador()), false);
    }

    private CtSolicitacaoProcedimento criarCorpo(SolicitacaoProcedimentoModel solicitacaoProcedimentoModel) {
        final CtSolicitacaoProcedimento corpo = new CtSolicitacaoProcedimento();

        CtContratadoDados ctContratadoDados = new CtContratadoDados();
        ctContratadoDados.setCodigoPrestadorNaOperadora(solicitacaoProcedimentoModel.getIdentificacaoPrestador().getCodigoPrestadorNaOperadora()); // (CD_CGC_PRESTADOR) Código identificador do prestador solicitante junto a operadora, conforme contrato estabelecido.
        ctContratadoDados.setNomeContratado(solicitacaoProcedimentoModel.getIdentificacaoPrestador().getNomePrestador()); // Não foi encontrado a coluna no banco de dados

        CtBeneficiarioDados ctBeneficiarioDados = new CtBeneficiarioDados();
        ctBeneficiarioDados.setNumeroCarteira(solicitacaoProcedimentoModel.getBeneficiario().getNumeroCarteira()); // (CARTAO_CLIENTE) Número da carteira do beneficiário na operadora
        ctBeneficiarioDados.setNomeBeneficiario(solicitacaoProcedimentoModel.getBeneficiario().getNome()); // Não foi encontrada a coluna no banco de dados
        ctBeneficiarioDados.setAtendimentoRN(solicitacaoProcedimentoModel.getBeneficiario().getAtendimentoRN()); // Não foi encontrada a coluna no banco de dados

        CtmSpSadtSolicitacaoGuia.DadosSolicitante dadosSolicitante = new CtmSpSadtSolicitacaoGuia.DadosSolicitante();
        dadosSolicitante.setContratadoSolicitante(ctContratadoDados);
        dadosSolicitante.setProfissionalSolicitante(solicitacaoProcedimentoModel.getCtContratadoProfissionalDados());

        CtGuiaCabecalho ctGuiaCabecalho = new CtGuiaCabecalho();
        ctGuiaCabecalho.setRegistroANS(solicitacaoProcedimentoModel.getNumeroANS());
        ctGuiaCabecalho.setNumeroGuiaPrestador(solicitacaoProcedimentoModel.getNumeroGuiaPrestador());

        CtProcedimentoDados ctProcedimentoDados = new CtProcedimentoDados();

        CtmSpSadtSolicitacaoGuia.ProcedimentosSolicitados procedimentosSolicitados = new CtmSpSadtSolicitacaoGuia.ProcedimentosSolicitados();
        procedimentosSolicitados.setProcedimento(ctProcedimentoDados);

        List<CtmSpSadtSolicitacaoGuia.ProcedimentosSolicitados> procedimentosSolicitadosList = new ArrayList<>();
        procedimentosSolicitadosList.add(procedimentosSolicitados);

        List<CtmSpSadtSolicitacaoGuia.ProcedimentosSolicitados> spSadtProcedimentos = solicitacaoProcedimentoModel.getProcedimentosSolicitadosList();
        solicitacaoProcedimentoModel.setProcedimentosSolicitadosList(procedimentosSolicitadosList);

        CtmSpSadtSolicitacaoGuia ctmSpSadtSolicitacaoGuia = new CtmSpSadtSolicitacaoGuia();
        ctmSpSadtSolicitacaoGuia.setDadosBeneficiario(ctBeneficiarioDados);
        ctmSpSadtSolicitacaoGuia.setDadosSolicitante(dadosSolicitante);
        ctmSpSadtSolicitacaoGuia.setDataSolicitacao(solicitacaoProcedimentoModel.getDataSolicitacao()); // Data em que o profissional está solicitando os procedimentos ou itens assistenciais.
        ctmSpSadtSolicitacaoGuia.setCaraterAtendimento(solicitacaoProcedimentoModel.getCaraterAtendimento()); // (IE_CARATER_INT_TISS) Código do caráter do atendimento, conforme tabela de domínio nº 23.
        ctmSpSadtSolicitacaoGuia.setIndicacaoClinica(solicitacaoProcedimentoModel.getIndicacaoClinica());
        ctmSpSadtSolicitacaoGuia.getProcedimentosSolicitados().addAll(spSadtProcedimentos);
        ctmSpSadtSolicitacaoGuia.setDadosExecutante(solicitacaoProcedimentoModel.getDadosExecutante());
        ctmSpSadtSolicitacaoGuia.setCabecalhoSolicitacao(ctGuiaCabecalho);
        ctmSpSadtSolicitacaoGuia.setTipoEtapaAutorizacao("2");
        ctmSpSadtSolicitacaoGuia.setNumeroGuiaPrincipal(solicitacaoProcedimentoModel.getNumeroGuiaPrincipal());

        corpo.setSolicitacaoSPSADT(ctmSpSadtSolicitacaoGuia);

        return corpo;
    }

    private CtContratadoDados criarContratadoDados(IdentificacaoPrestadorModel identificacao, String registroAns) {
        final CtContratadoDados contratadoDados = new CtContratadoDados();
        definirIdentificacaoPrestador(identificacao, contratadoDados::setCodigoPrestadorNaOperadora,
                contratadoDados::setCpfContratado, contratadoDados::setCnpjContratado);

        contratadoDados.setNomeContratado(identificacao.getNomePrestador());

        return contratadoDados;
    }

    public void definirIdentificacaoPrestador(IdentificacaoPrestadorModel identificacao, Consumer<String> setCodigo, Consumer<String> setCPF,
                                              Consumer<String> setCNPJ) {
        final String cnpj = identificacao.getCnpj();
        if (!Strings.isNullOrEmpty(cnpj)) {
            setCNPJ.accept(cnpj);
            return;
        }

        final String cpf = identificacao.getCpf();
        if (!Strings.isNullOrEmpty(cpf)) {
            setCPF.accept(cpf);
            return;
        }

        final String prestadorOperadoraId = identificacao.getCodigoPrestadorNaOperadora();
        if (!Strings.isNullOrEmpty(prestadorOperadoraId)) {
            setCodigo.accept(prestadorOperadoraId);
        }
    }
}





