package br.com.spintec.wstiss.core.config;

public interface InterfaceTissWS<PortType, Response> {
    Response apply(PortType t);
}
