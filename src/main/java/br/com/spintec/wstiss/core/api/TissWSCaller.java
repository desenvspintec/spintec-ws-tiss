package br.com.spintec.wstiss.core.api;

import br.com.spintec.wstiss.core.config.InterfaceTissWS;
import br.gov.ans.padroes.tiss.schemas.v30500.CabecalhoTransacao;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class TissWSCaller<PortType, Response> {

    private static final String FORCE_START_DOCUMENT = "org.apache.cxf.stax.force-start-document";
    private static final String ENCODING = "ISO-8859-1";

    public Response chamarWS(Class<PortType> portTypeClass, CabecalhoTransacao cabecalho, String urlService, String urlWSDLDocumentLocation,
                             QName serviceName, QName portName, InterfaceTissWS<PortType, Response> function) throws Throwable {

        return executeWSCall(portTypeClass, urlService, urlWSDLDocumentLocation, serviceName, portName, function, cabecalho.getDestino().getRegistroANS());
    }


    private Response executeWSCall(Class<PortType> portTypeClass, String urlService, String urlWSDLDocumentLocation, QName serviceName,
                                   QName portName, InterfaceTissWS<PortType, Response> function, String registroAns) throws Throwable {
        Response ret = null;

        final Service service = createService(urlWSDLDocumentLocation, serviceName);

        final PortType port = service.getPort(portTypeClass);
        configurePort(port, urlService, registroAns);

        ret = function.apply(port);

        return ret;
    }


    protected Service createService(String urlWSDLDocumentLocation, QName serviceName) throws Throwable {
        final URL wsdlURL = new URL(urlWSDLDocumentLocation);
        Service service = Service.create(wsdlURL, serviceName);
        return service;
    }


    protected Client createClient(PortType port) {
        return ClientProxy.getClient(port);
    }

    private void configurePort(PortType port, String urlService, String registroAns) {

        final Client client = createClient(port);

        client.getInInterceptors().add(new org.apache.cxf.interceptor.LoggingInInterceptor());
        client.getOutInterceptors().add(new org.apache.cxf.interceptor.LoggingOutInterceptor());

        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, urlService);
        client.getRequestContext().put(Message.ENCODING, ENCODING);
        client.getRequestContext().put(FORCE_START_DOCUMENT, Boolean.TRUE);
    }
}