package br.com.spintec.wstiss.core.api;

import br.gov.ans.padroes.tiss.schemas.v30500.ISolicitacao;
import br.gov.ans.padroes.tiss.schemas.v30500.MensagemTISS;
import br.gov.ans.padroes.tiss.schemas.v30500.PedidoSolicitacaoProcedimentoWSI;
import br.gov.ans.padroes.tiss.schemas.v30500.SolicitacaoProcedimentoWS;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.*;
import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class CustomWSCaller<MensagemWS extends PedidoSolicitacaoProcedimentoWSI, Response> {
    public Response send(String url, MensagemWS param) throws IOException, JAXBException, SOAPException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(_convertToParam(param));
        wr.flush();
        wr.close();

        String statusResponse = con.getResponseMessage();
        log.info("Status Response: {}", statusResponse);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        log.info("Response Data: {}", response.toString());

        return _convertToResponse(response.toString());
    }

    private String _convertToParam(MensagemWS message) throws JAXBException, SOAPException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(SolicitacaoProcedimentoWS.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        StringWriter sw = new StringWriter();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPBody soapBody = soapMessage.getSOAPBody();

        /*sw.write("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sch=\"http://www.ans.gov.br/padroes/tiss/schemas\">");
        sw.append("<soapenv:Header/>");*/
        // jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.ans.gov.br/padroes/tiss/schemas http://www.ans.gov.br/padroes/tiss/schemas/tissV3_05_00.xsd");

        jaxbMarshaller.marshal(message, sw);

        String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "\t<soapenv:Header/>\n" +
                "\t<soapenv:Body>" + sw.toString() + "</soapenv:Body>\n" +
                "</soapenv:Envelope>";

        log.info("XML-Content: {}", xml);

        return xml;
    }

    @SuppressWarnings("unchecked")
    private Response _convertToResponse(String strResponse) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance((Class<Response>)
                ((ParameterizedType)getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[0]);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        Response response = (Response)jaxbUnmarshaller.unmarshal(new StringReader(strResponse));

        return response;
    }
}
