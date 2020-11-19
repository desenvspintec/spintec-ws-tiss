package br.com.spintec.wstiss.service.ws.config;

import br.com.spintec.wstiss.annotation.TissVersion;
import br.com.spintec.wstiss.core.config.Configuration;
import br.com.spintec.wstiss.core.config.InterfaceTissWS;
import br.com.spintec.wstiss.core.config.MensagemTissWSConfig;
import br.gov.ans.padroes.tiss.schemas.api.PedidoSolicitacaoProcedimentoWSI;
import br.gov.ans.padroes.tiss.schemas.api.RespostaAutorizacaoProcedimentoWSI;
import br.gov.ans.padroes.tiss.schemas.SolicitacaoProcedimentoWS;
import br.gov.ans.tiss.ws.tipos.tisssolicitacaoprocedimento.v30500.TissFault;
import br.gov.ans.tiss.ws.tipos.tisssolicitacaoprocedimento.v30500.TissSolicitacaoProcedimentoPortType;

import javax.xml.namespace.QName;

@TissVersion(tipoMensagem = PedidoSolicitacaoProcedimentoWSI.class, versao = "3.05.00")
public class SolicitacaoProcedimentoConfig
        implements MensagemTissWSConfig<TissSolicitacaoProcedimentoPortType, RespostaAutorizacaoProcedimentoWSI, PedidoSolicitacaoProcedimentoWSI> {

    private String urlWs = Configuration.getWsUrl("/tissSolicitacaoProcedimento");

    private String urlWsdl = Configuration.getWsUrl("/tissSolicitacaoProcedimento?wsdl");


    @Override
    public Class<TissSolicitacaoProcedimentoPortType> getTissPortTypeClass() {
        return TissSolicitacaoProcedimentoPortType.class;
    }

    @Override
    public String getUrlWs() {
        return urlWs;
    }

    @Override
    public String getUrlWsdl() {
        return urlWsdl;
    }

    @Override
    public QName getServiceName() {
        return new QName("http://www.ans.gov.br/tiss/ws/tipos/tisssolicitacaoprocedimento/v30500", "tissSolicitacaoProcedimento");
    }

    @Override
    public QName getPortName() {
        return new QName("tissSolicitacaoProcedimento_Port");
    }

    @Override
    public InterfaceTissWS<TissSolicitacaoProcedimentoPortType, RespostaAutorizacaoProcedimentoWSI> getCallerFunciton(PedidoSolicitacaoProcedimentoWSI mensagemTissWSI) {
        return caller -> {
            try {
                return caller.tissSolicitacaoProcedimentoOperation((SolicitacaoProcedimentoWS) mensagemTissWSI);
            } catch (final TissFault tissFault) {
                throw new RuntimeException(tissFault);
            }
        };
    }
}
