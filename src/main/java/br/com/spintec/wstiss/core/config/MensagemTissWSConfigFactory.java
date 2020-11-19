package br.com.spintec.wstiss.core.config;

import br.com.spintec.wstiss.annotation.TissVersion;
import br.com.spintec.wstiss.annotation.TissVersionQualifier;
import br.gov.ans.padroes.tiss.schemas.api.MensagemTissWSI;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class MensagemTissWSConfigFactory {

    @SuppressWarnings("unchecked")
    public <PortType, Response, Mensagem> MensagemTissWSConfig<PortType, Response, Mensagem>
    getConfiguration(Class<? extends MensagemTissWSI> tipoMensagem, String versao) {
        final List<MensagemTissWSConfig<?, ?, ?>> selected = select(new TissVersionQualifier(tipoMensagem, versao));

        if (selected.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("NÃO EXISTE CONFIGURAÇÃO DO WEB SERVICE TISS DE [%s] NA VERSÃO [%s]", tipoMensagem.getSimpleName(), versao));
        }
        if (selected.size() > 1) {
            throw new IllegalArgumentException(String.format("EXISTE MAIS DE UMA CONFIGURAÇÃO DO WEB SERVICE TISS DE [%s] NA VERSÃO [%s]",
                    tipoMensagem.getSimpleName(), versao));
        }

        return (MensagemTissWSConfig<PortType, Response, Mensagem>) selected.get(0);
    }

    private List<MensagemTissWSConfig<?, ?, ?>>select(TissVersionQualifier tissVersionQualifier) {
        Reflections reflections = new Reflections("br.com.spintec.wstiss.service.ws.config");
        Set<Class<?>> tissVersionClasses = reflections.getTypesAnnotatedWith(TissVersion.class);

        List<MensagemTissWSConfig<?, ?, ?>> lista = new ArrayList<>();
        tissVersionClasses.forEach(tvc -> {
            TissVersion version = tvc.getAnnotation(TissVersion.class);
            boolean equals = tissVersionQualifier.equals(version);
            if(equals) {
                try {
                    lista.add((MensagemTissWSConfig<?, ?, ?>) tvc.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    log.error("Erro interno ao instanciar a classe builder. [class={}]", tvc.getName());
                }
            }
        });

        return lista;
    }
}