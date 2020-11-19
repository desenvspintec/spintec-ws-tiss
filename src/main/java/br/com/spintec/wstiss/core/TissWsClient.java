package br.com.spintec.wstiss.core;

import br.com.spintec.wstiss.core.api.TissWSCaller;
import br.com.spintec.wstiss.core.builder.MensagemTissWSBuilder;
import br.com.spintec.wstiss.core.builder.MensagemTissWsBuilderFactory;
import br.com.spintec.wstiss.core.config.MensagemTissWSConfig;
import br.com.spintec.wstiss.core.config.MensagemTissWSConfigFactory;
import br.gov.ans.padroes.tiss.schemas.api.CabecalhoTransacaoTISSI;
import br.gov.ans.padroes.tiss.schemas.api.MensagemTissWSI;

import java.util.Optional;

public class TissWsClient<MensagemWS extends MensagemTissWSI, MensagemObj, Response> {

    private final MensagemTissWsBuilderFactory builderFactory = new MensagemTissWsBuilderFactory();

    private final MensagemTissWSConfigFactory configFactory = new MensagemTissWSConfigFactory();

    private TissWSCaller<Object, Response> wsCaller = new TissWSCaller<>();

    public Response chamarWS(MensagemObj obj, Class<MensagemWS> tipoMensagem, String versao) throws Throwable {
        final MensagemTissWSBuilder<MensagemWS, MensagemObj> builder = builderFactory.getBuilder(tipoMensagem, versao);
        final MensagemWS mensagemTiss = builder.builder(obj);

        final MensagemTissWSConfig<Object, Response, MensagemWS> conf = configFactory.getConfiguration(tipoMensagem, versao);
        return chamarWS(mensagemTiss, conf);
    }

    private Response chamarWS(MensagemWS mensagemTiss, MensagemTissWSConfig<Object, Response, MensagemWS> conf) throws Throwable {
        final CabecalhoTransacaoTISSI cabecalho = mensagemTiss.getCabecalho();
        Optional.ofNullable(cabecalho).map(CabecalhoTransacaoTISSI::getNrANSDestino)
                .orElseThrow(() -> new IllegalArgumentException("O registro ANS da operadora precisa ser definido no cabeçalho da mensagem TISS"));

        final String wsUrl = conf.getUrlWs();
        final String wsdlUrl = conf.getUrlWsdl();

        return wsCaller.chamarWS(conf.getTissPortTypeClass(), cabecalho, wsUrl, wsdlUrl, conf.getServiceName(), conf.getPortName(),
                conf.getCallerFunciton(mensagemTiss));
    }
}
