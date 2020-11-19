package br.com.spintec.wstiss.annotation;

import br.gov.ans.padroes.tiss.schemas.api.MensagemTissWSI;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface TissVersion {

    Class<? extends MensagemTissWSI> tipoMensagem();

    String versao();
}
