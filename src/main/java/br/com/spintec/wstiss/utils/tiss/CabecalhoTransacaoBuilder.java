package br.com.spintec.wstiss.utils.tiss;

import br.com.spintec.wstiss.model.IdentificacaoPrestadorModel;
import br.com.spintec.wstiss.utils.DateUtils;
import br.gov.ans.padroes.tiss.schemas.CabecalhoTransacao;
import br.gov.ans.padroes.tiss.schemas.CabecalhoTransacao.Destino;
import br.gov.ans.padroes.tiss.schemas.CabecalhoTransacao.IdentificacaoTransacao;
import br.gov.ans.padroes.tiss.schemas.CabecalhoTransacao.Origem;
import br.gov.ans.padroes.tiss.schemas.CabecalhoTransacao.Origem.IdentificacaoPrestador;
import br.gov.ans.padroes.tiss.schemas.CtPrestadorIdentificacao;
import br.gov.ans.padroes.tiss.schemas.DmTipoTransacao;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.Optional;

public class CabecalhoTransacaoBuilder {

    private final EstruturaComumBuilder comumBuilder = new EstruturaComumBuilder();

    public CabecalhoTransacao criarCabecalho(DmTipoTransacao tipoTransacao, String sequencialTransacao, String registroAnsOperadora,
                                             Optional<IdentificacaoPrestadorModel> identificacao, boolean incluirLoginSenha) {
        return criarCabecalho(tipoTransacao, Optional.ofNullable(sequencialTransacao), registroAnsOperadora, identificacao, incluirLoginSenha);
    }

    public CabecalhoTransacao criarCabecalho(DmTipoTransacao tipoTransacao, Optional<String> sequencialTransacaoOpt, String registroAnsOperadora,
                                             Optional<IdentificacaoPrestadorModel> identificacao, boolean incluirLoginSenha) {

        final CabecalhoTransacao cabecalho = new CabecalhoTransacao();
        cabecalho.setPadrao("3.05.00");
        cabecalho.setIdentificacaoTransacao(criarIdentificacaoTransacao(sequencialTransacaoOpt, tipoTransacao));
        cabecalho.setOrigem(criarOrigem(registroAnsOperadora, identificacao));
        cabecalho.setDestino(criarDestino(registroAnsOperadora, identificacao));
        if (incluirLoginSenha) {
            //cabecalho.setLoginSenhaPrestador(criarLoginSenhaPrestador(registroAnsOperadora));
        }
        return cabecalho;
    }

    private IdentificacaoTransacao criarIdentificacaoTransacao(Optional<String> sequencialTransacaoOpt, DmTipoTransacao tipoTransacao) {
        final IdentificacaoTransacao identificacaoTransacao = new IdentificacaoTransacao();
        identificacaoTransacao.setSequencialTransacao(
                sequencialTransacaoOpt.map(seq -> String.format("%.12s", seq)).orElse(String.format("%.12s", System.currentTimeMillis())));
        identificacaoTransacao.setTipoTransacao(tipoTransacao);

        final XMLGregorianCalendar dataTransacaoXGC = DateUtils.getXMLGregCalendarWithOutTimeZoneAndMillisecond(new Date());
        identificacaoTransacao.setDataRegistroTransacao(dataTransacaoXGC);
        identificacaoTransacao.setHoraRegistroTransacao(dataTransacaoXGC);

        return identificacaoTransacao;
    }

    private IdentificacaoPrestador criarIdentificacaoPrestador(IdentificacaoPrestadorModel identificacao) {
        final IdentificacaoPrestador identificadorPrestador = new IdentificacaoPrestador();

        comumBuilder.definirIdentificacaoPrestador(identificacao, identificadorPrestador::setCodigoPrestadorNaOperadora,
                identificadorPrestador::setCPF, identificadorPrestador::setCNPJ);

        return identificadorPrestador;
    }

    private CtPrestadorIdentificacao criarCtPrestadorIdentificacao(IdentificacaoPrestadorModel identificacao) {
        final CtPrestadorIdentificacao identificadorPrestador = new CtPrestadorIdentificacao();

        comumBuilder.definirIdentificacaoPrestador(identificacao, identificadorPrestador::setCodigoPrestadorNaOperadora,
                identificadorPrestador::setCPF, identificadorPrestador::setCNPJ);

        return identificadorPrestador;
    }

    private Origem criarOrigem(String registroAnsOperadora, Optional<IdentificacaoPrestadorModel> identificacaoOpt) {
        final Origem origem = new Origem();
        origem.setRegistroANS(formatarRegistroAns(registroAnsOperadora));
        identificacaoOpt.map(this::criarIdentificacaoPrestador).ifPresent(origem::setIdentificacaoPrestador);
        return origem;
    }

    private Destino criarDestino(String registroAnsOperadora, Optional<IdentificacaoPrestadorModel> identificacaoOpt) {
        final Destino destino = new Destino();
        destino.setRegistroANS(formatarRegistroAns(registroAnsOperadora));
        identificacaoOpt.map(this::criarCtPrestadorIdentificacao).ifPresent(destino::setIdentificacaoPrestador);
        return destino;
    }

    private String formatarRegistroAns(String registroAnsOperadora) {
        if(registroAnsOperadora != null) {
            return String.format("%06d", Integer.parseInt(registroAnsOperadora));
        }
        return null;
    }
}