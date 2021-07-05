package br.com.spintec.wstiss.utils.tiss;

import java.util.Date;
import java.util.Optional;

import javax.xml.datatype.XMLGregorianCalendar;

import br.com.spintec.wstiss.model.IdentificacaoPrestadorModel;
import br.com.spintec.wstiss.utils.DateUtils;
import br.gov.ans.padroes.tiss.schemas.v30500.CabecalhoTransacao;
import br.gov.ans.padroes.tiss.schemas.v30500.CtPrestadorIdentificacao;
import br.gov.ans.padroes.tiss.schemas.v30500.DmTipoTransacao;

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

    private CabecalhoTransacao.IdentificacaoTransacao criarIdentificacaoTransacao(Optional<String> sequencialTransacaoOpt, DmTipoTransacao tipoTransacao) {
        final CabecalhoTransacao.IdentificacaoTransacao identificacaoTransacao = new CabecalhoTransacao.IdentificacaoTransacao();
        identificacaoTransacao.setSequencialTransacao(
                sequencialTransacaoOpt.map(seq -> String.format("%.12s", seq)).orElse(String.format("%.12s", System.currentTimeMillis())));
        identificacaoTransacao.setTipoTransacao(tipoTransacao);

        final XMLGregorianCalendar dataTransacaoXGC = DateUtils.getXMLGregCalendarWithOutTimeZoneAndMillisecond(new Date());
        identificacaoTransacao.setDataRegistroTransacao(dataTransacaoXGC);
        identificacaoTransacao.setHoraRegistroTransacao(dataTransacaoXGC);

        return identificacaoTransacao;
    }

    private CabecalhoTransacao.Origem.IdentificacaoPrestador criarIdentificacaoPrestador(IdentificacaoPrestadorModel identificacao) {
        final CabecalhoTransacao.Origem.IdentificacaoPrestador identificadorPrestador = new CabecalhoTransacao.Origem.IdentificacaoPrestador();

        comumBuilder.definirIdentificacaoPrestador(identificacao, identificadorPrestador::setCodigoPrestadorNaOperadora,
                identificadorPrestador::setCPF, identificadorPrestador::setCNPJ);

        return identificadorPrestador;
    }

    private CabecalhoTransacao.Origem criarOrigem(String registroAnsOperadora, Optional<IdentificacaoPrestadorModel> identificacaoOpt) {
        final CabecalhoTransacao.Origem origem = new CabecalhoTransacao.Origem();
        // origem.setRegistroANS(formatarRegistroAns(registroAnsOperadora));
        identificacaoOpt.map(this::criarIdentificacaoPrestador).ifPresent(origem::setIdentificacaoPrestador);
        return origem;
    }

    private CabecalhoTransacao.Destino criarDestino(String registroAnsOperadora, Optional<IdentificacaoPrestadorModel> identificacaoOpt) {
        final CabecalhoTransacao.Destino destino = new CabecalhoTransacao.Destino();
        destino.setRegistroANS(formatarRegistroAns(registroAnsOperadora));
        // identificacaoOpt.map(this::criarCtPrestadorIdentificacao).ifPresent(destino::setIdentificacaoPrestador);
        return destino;
    }

    private String formatarRegistroAns(String registroAnsOperadora) {
        if(registroAnsOperadora != null) {
            return String.format("%06d", Integer.parseInt(registroAnsOperadora));
        }
        return null;
    }
 
    private CtPrestadorIdentificacao criarCtPrestadorIdentificacao(IdentificacaoPrestadorModel identificacao) {
        final CtPrestadorIdentificacao identificadorPrestador = new CtPrestadorIdentificacao();

        comumBuilder.definirIdentificacaoPrestador(identificacao, identificadorPrestador::setCodigoPrestadorNaOperadora,
                identificadorPrestador::setCPF, identificadorPrestador::setCNPJ);

        return identificadorPrestador;
    }
}