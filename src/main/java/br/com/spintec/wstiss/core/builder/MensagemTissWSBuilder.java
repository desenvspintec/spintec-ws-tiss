package br.com.spintec.wstiss.core.builder;

public interface MensagemTissWSBuilder<MensagemWS, MensagemObj> {
    MensagemWS builder(MensagemObj mensagemObj);
}
