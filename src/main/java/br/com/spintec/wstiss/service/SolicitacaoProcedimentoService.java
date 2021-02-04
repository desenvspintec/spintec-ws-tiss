package br.com.spintec.wstiss.service;

import br.com.spintec.wstiss.core.TissWsClient;
import br.com.spintec.wstiss.model.SolicitacaoProcedimentoModel;
import br.com.spintec.wstiss.model.response.SolicitacaoProcedimentoResponseModel;
import br.gov.ans.padroes.tiss.schemas.v30500.AutorizacaoProcedimentoWS;
import br.gov.ans.padroes.tiss.schemas.v30500.MensagemTISS;

import java.util.Arrays;
import java.util.List;

public class SolicitacaoProcedimentoService {
    private TissWsClient<MensagemTISS, SolicitacaoProcedimentoModel, AutorizacaoProcedimentoWS> clientWS = new TissWsClient<>();

    public SolicitacaoProcedimentoResponseModel<AutorizacaoProcedimentoWS> enviarSolicitacao(SolicitacaoProcedimentoModel solicitacaoProcedimento) throws Exception {
        SolicitacaoProcedimentoResponseModel<AutorizacaoProcedimentoWS> retorno = new SolicitacaoProcedimentoResponseModel<>();
        retorno.setVersaoTISS("3.05.00");
        final AutorizacaoProcedimentoWS respostaSolicitacao = clientWS.chamarWS(solicitacaoProcedimento, MensagemTISS.class, "3.05.00");
        if (respostaSolicitacao.getAutorizacaoProcedimento() != null && respostaSolicitacao.getAutorizacaoProcedimento().getMensagemErro() != null) {
            retorno.setSucesso(false);
        }
        retorno.setRetornoSolicitacaoProcedimento(respostaSolicitacao);
        return retorno;
    }

    private void preencherRetorno(SolicitacaoProcedimentoResponseModel<AutorizacaoProcedimentoWS> retorno, Exception e) {
        e.setStackTrace(tratarStackTrace(e));
        retorno.setSucesso(false);
        retorno.setErro(e);
        retorno.setMessage(e.getMessage());
    }

    private StackTraceElement[] tratarStackTrace(Throwable e) {
        List<StackTraceElement> stack = Arrays.asList(e.getStackTrace());
        if(stack.size() > 3) {
            List<StackTraceElement> subStack = stack.subList(0, 3);
            return subStack.toArray(new StackTraceElement[3]);
        } else {
            return e.getStackTrace();
        }
    }
}
