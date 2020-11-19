package br.com.spintec.wstiss.utils;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public final class DateUtils {

    private DateUtils() {
    }

    /**
     * Seta as horas para o inicio do dia
     *
     * @param date
     * @return
     */
    public static Date horasInicial(Date date) {
        return toDate(toLocalDate(date).atStartOfDay());
    }

    /**
     * Seta as horas para o final do dia
     *
     * @param date
     * @return
     */
    public static Date horasFinal(Date date) {
        return toDate(toLocalDate(date).atTime(23, 59, 59));
    }

    public static Date inicioDoAno(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
        return calendar.getTime();
    }

    public static Date inicioDoMes(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * Método facilitador que retorna uma cópia da data passada em parâmetro.&lt;/br&gt;
     * Caso o valor do parâmetro esteja nulo, é retornado nulo.
     *
     * @param dateToClone Data que se deseja copiar
     * @return Cópia da data passada em parâmetro
     */
    public static Date cloneDate(Date dateToClone) {
        return dateToClone == null ? null : new Date(dateToClone.getTime());
    }

    public static Date calcularData(Date data, int dateField, Integer quant) {
        if (data == null) {
            return null;
        }
        if (quant == null) {
            return data;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(dateField, quant);
        return calendar.getTime();
    }

    /**
     *
     * Retorna uma string após calcular Horas e Minutos em um intervalo de datas.
     */
    public static String calcularHorasIntervaloDatas(Date dataInicio, Date dataFim) {

        final LocalDateTime dtInicio = toLocalDateTime(dataInicio);
        final LocalDateTime dtFim = toLocalDateTime(dataFim);

        final Duration duracao = Duration.between(dtInicio, dtFim);
        final long dias = duracao.toDays();
        final long hora = duracao.minusDays(dias).toHours();
        final long minuto = duracao.minusDays(dias).minusHours(hora).toMinutes();

        String format = "";
        if (dias > 0) {
            format = Long.toString(dias) + " dia(s) ";
        }
        if (hora > 0) {
            format = format + Long.toString(hora) + " hora(s) ";
        }
        if (minuto > 0) {
            format = format + Long.toString(minuto) + " min";
        }
        return format;
    }

    /**
     * Retorna uma string após calcular Horas e Minutos em um intervalo de datas.
     */
    public static String calcularHorasMinutosSegundosIntervaloDatas(Date dataInicio, Date dataFim) {

        final LocalDateTime dtInicio = toLocalDateTime(dataInicio);
        final LocalDateTime dtFim = toLocalDateTime(dataFim);

        final Duration duracao = Duration.between(dtInicio, dtFim);

        return calcularHorasMinutosSegundosIntervaloDatas(duracao);
    }

    public static String calcularHorasMinutosSegundosIntervaloDatas(Duration duracao) {
        final long dias = duracao.toDays();
        final long hora = duracao.minusDays(dias).toHours();
        final long minuto = duracao.minusDays(dias).minusHours(hora).toMinutes();
        final long segundos = duracao.minusDays(dias).minusHours(hora).minusMinutes(minuto).getSeconds();

        String format = "";
        if (dias > 0) {
            format = Long.toString(dias) + " dia(s) ";
        }
        if (hora > 0) {
            format = format + Long.toString(hora) + " hora(s) ";
        }
        if (minuto > 0) {
            format = format + Long.toString(minuto) + " min ";
        }
        if (segundos > 0) {
            format = format + Long.toString(segundos) + " seg";
        }
        return format;
    }

    public static String formatToString(Date date, String format) {
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Long segundosEntre(Date dataInicial, Date dataFinal) {
        return Duration.between(toLocalDateTime(dataInicial), toLocalDateTime(dataFinal)).getSeconds();
    }

    /**
     * Faz merge de um date com um datetime
     *
     * @param date
     * @param time
     * @return
     */
    public static Date mergeDateTime(Date date, Date time) {
        if (date != null && time != null) {
            final Calendar dCal = Calendar.getInstance();
            dCal.setTime(date);
            final Calendar tCal = Calendar.getInstance();
            tCal.setTime(time);
            dCal.set(Calendar.HOUR_OF_DAY, tCal.get(Calendar.HOUR_OF_DAY));
            dCal.set(Calendar.MINUTE, tCal.get(Calendar.MINUTE));
            dCal.set(Calendar.SECOND, tCal.get(Calendar.SECOND));
            dCal.set(Calendar.MILLISECOND, tCal.get(Calendar.MILLISECOND));
            return dCal.getTime();
        }
        return null;
    }

    public static XMLGregorianCalendar getXMLGregCalendarWithOutTimeZoneAndMillisecond(Date data) {
        try {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(data);
            calendar.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));

            final XMLGregorianCalendar xmlcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            xmlcal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            xmlcal.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
            return xmlcal;
        } catch (final Exception exp) {
            throw new RuntimeException("ERRO AO CONFIGURAR A DATA/HORA DO WEB SERVICE", exp);
        }
    }
}

