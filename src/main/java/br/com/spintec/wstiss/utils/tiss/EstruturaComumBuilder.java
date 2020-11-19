package br.com.spintec.wstiss.utils.tiss;

import br.com.spintec.wstiss.model.IdentificacaoPrestadorModel;
import br.com.spintec.wstiss.utils.DateUtils;
import com.google.common.base.Strings;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Consumer;

public class EstruturaComumBuilder {

	public XMLGregorianCalendar getXMLGregorianCalendar(Date data) {
        final GregorianCalendar dataGC = new GregorianCalendar();
		dataGC.setTime(data);
		return DateUtils.getXMLGregCalendarWithOutTimeZoneAndMillisecond(data);
    }
	
	public void definirIdentificacaoPrestador(IdentificacaoPrestadorModel identificacao, Consumer<String> setCodigo, Consumer<String> setCPF,
                                              Consumer<String> setCNPJ) {
        final String cnpj = identificacao.getCnpj();
        if (!Strings.isNullOrEmpty(cnpj)) {
            setCNPJ.accept(cnpj);
            return;
        }

        final String cpf = identificacao.getCpf();
        if (!Strings.isNullOrEmpty(cpf)) {
            setCPF.accept(cpf);
            return;
        }

        final String prestadorOperadoraId = identificacao.getCodigoPrestadorNaOperadora();
        if (!Strings.isNullOrEmpty(prestadorOperadoraId)) {
            setCodigo.accept(prestadorOperadoraId);
        }
    }
}
