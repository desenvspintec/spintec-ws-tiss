package br.com.spintec.wstiss.core.builder;

import br.com.spintec.wstiss.annotation.TissVersion;
import br.com.spintec.wstiss.annotation.TissVersionQualifier;
import br.com.spintec.wstiss.exception.BuilderException;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class MensagemTissWsBuilderFactory {

    @SuppressWarnings("unchecked")
    public <MensagemWS, MensagemObj> MensagemTissWSBuilder<MensagemWS, MensagemObj> getBuilder(Class<?> tipoMensagem,
                                                                                               String versao) {
        final List<MensagemTissWSBuilder<?, ?>> selected = select(new TissVersionQualifier(tipoMensagem, versao));

        if (selected.isEmpty()) {
            throw new BuilderException(
                    String.format("NÃO EXISTE BUILDER DO WEB SERVICE TISS DE [%s] NA VERSÃO [%s]", tipoMensagem.getSimpleName(), versao));
        }
        if (selected.size() > 1) {
            throw new BuilderException(
                    String.format("EXISTE MAIS DE UM BUILDER DO WEB SERVICE TISS DE [%s] NA VERSÃO [%s]", tipoMensagem.getSimpleName(), versao));
        }

        return (MensagemTissWSBuilder<MensagemWS, MensagemObj>) selected.get(0);
    }

    private List<MensagemTissWSBuilder<?, ?>> select(TissVersionQualifier tissVersionQualifier) {
        Reflections reflections = new Reflections("br.com.spintec.wstiss.service.ws.builder");
        Set<Class<?>> tissVersionClasses = reflections.getTypesAnnotatedWith(TissVersion.class);

        List<MensagemTissWSBuilder<?, ?>> lista = new ArrayList<>();
        tissVersionClasses.forEach(tvc -> {
            TissVersion version = tvc.getAnnotation(TissVersion.class);
            boolean equals = tissVersionQualifier.equals(version);
            if(equals) {
                try {
                    lista.add((MensagemTissWSBuilder<?, ?>) tvc.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    log.error("Erro interno ao instanciar a classe builder. [class={}]", tvc.getName());
                }
            }
        });
        return lista;
    }
}
