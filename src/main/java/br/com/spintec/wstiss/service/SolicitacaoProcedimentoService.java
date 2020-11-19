package br.com.spintec.wstiss.service;

import br.com.spintec.wstiss.core.TissWsClient;
import br.com.spintec.wstiss.model.SolicitacaoProcedimentoModel;
import br.com.spintec.wstiss.model.response.SolicitacaoProcedimentoResponseModel;
import br.gov.ans.padroes.tiss.schemas.api.PedidoSolicitacaoProcedimentoWSI;
import br.gov.ans.padroes.tiss.schemas.api.RespostaAutorizacaoProcedimentoWSI;

import java.util.Arrays;
import java.util.List;

public class SolicitacaoProcedimentoService {
    private TissWsClient<PedidoSolicitacaoProcedimentoWSI, SolicitacaoProcedimentoModel, RespostaAutorizacaoProcedimentoWSI> clientWS = new TissWsClient<>();

    public SolicitacaoProcedimentoResponseModel<RespostaAutorizacaoProcedimentoWSI> enviarSolicitacao(SolicitacaoProcedimentoModel solicitacaoProcedimento) {
        SolicitacaoProcedimentoResponseModel<RespostaAutorizacaoProcedimentoWSI> retorno = new SolicitacaoProcedimentoResponseModel<>();
        try {
            retorno.setVersaoTISS("3.05.00");
            final RespostaAutorizacaoProcedimentoWSI respostaSolicitacao = clientWS.chamarWS(solicitacaoProcedimento, PedidoSolicitacaoProcedimentoWSI.class, "3.05.00");
            retorno.setSucesso(true);
            retorno.setRetornoSolicitacaoProcedimento(respostaSolicitacao);
        } catch (Throwable re) {
            preencherRetorno(retorno, re);
        }
        return retorno;
    }

    private void preencherRetorno(SolicitacaoProcedimentoResponseModel<RespostaAutorizacaoProcedimentoWSI> retorno, Throwable e) {
        e.setStackTrace(tratarStackTrace(e));
        retorno.setSucesso(false);
        retorno.setErro(e);
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
