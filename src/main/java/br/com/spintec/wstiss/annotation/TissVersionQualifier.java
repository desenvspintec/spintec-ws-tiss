package br.com.spintec.wstiss.annotation;

import javax.enterprise.util.AnnotationLiteral;

public class TissVersionQualifier extends AnnotationLiteral<TissVersion> implements TissVersion {

    /**
     *
     */
    private static final long serialVersionUID = 4699092746227994838L;

    private final Class<?> tipoMensagemWS;
    private final String versaoTiss;

    public TissVersionQualifier(Class<?> tipoMensagemWS, String versaoTiss) {
        this.tipoMensagemWS = tipoMensagemWS;
        this.versaoTiss = versaoTiss;
    }

    @Override
    public Class<?> tipoMensagem() {
        return tipoMensagemWS;
    }

    @Override
    public String versao() {
        return versaoTiss;
    }
}
