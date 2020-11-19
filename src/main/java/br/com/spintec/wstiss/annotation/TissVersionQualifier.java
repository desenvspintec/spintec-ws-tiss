package br.com.spintec.wstiss.annotation;

import br.gov.ans.padroes.tiss.schemas.api.MensagemTissWSI;

import javax.enterprise.util.AnnotationLiteral;

public class TissVersionQualifier extends AnnotationLiteral<TissVersion> implements TissVersion {

    /**
     *
     */
    private static final long serialVersionUID = 4699092746227994838L;

    private final Class<? extends MensagemTissWSI> tipoMensagemWS;
    private final String versaoTiss;

    public TissVersionQualifier(Class<? extends MensagemTissWSI> tipoMensagemWS, String versaoTiss) {
        this.tipoMensagemWS = tipoMensagemWS;
        this.versaoTiss = versaoTiss;
    }

    @Override
    public Class<? extends MensagemTissWSI> tipoMensagem() {
        return tipoMensagemWS;
    }

    @Override
    public String versao() {
        return versaoTiss;
    }
}
