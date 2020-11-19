package br.com.spintec.wstiss.core.config;

import javax.xml.namespace.QName;

public interface MensagemTissWSConfig<PortType, Response, Mensagem> {

    Class<PortType> getTissPortTypeClass();

    String getUrlWs();

    String getUrlWsdl();

    QName getServiceName();

    QName getPortName();

    InterfaceTissWS<PortType, Response> getCallerFunciton(Mensagem mensagem);
}
