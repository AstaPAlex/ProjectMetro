import exceptions.*;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;
import metro.Color;
import metro.Metro;
import metro.Station;


public class Runner {
    public static void main(String[] args) throws ImpossibleCreateLineException, ImpossibleCreateStationException,
            NameStationException, StartEqualsFinishException {
        Metro metro = new Metro("Пермь");
        addStationMetro(metro);
        System.out.println(metro);
    }

    public static void addStationMetro(Metro metro) throws ImpossibleCreateLineException,
            ImpossibleCreateStationException, NameStationException, StartEqualsFinishException {
        metro.createLine(Color.RED);
        Station station01 = metro.createFirstStation(Color.RED, "Спортивная");
        metro.createFinallyStation(Color.RED, "Медведковская", Duration.ofSeconds(141));
        metro.createFinallyStation(Color.RED, "Молодежная", Duration.ofSeconds(118));
        metro.createFinallyStation(Color.RED, "Пермь 1", Duration.ofSeconds(180));
        metro.createFinallyStation(Color.RED, "Пермь 2", Duration.ofSeconds(130));
        metro.createFinallyStation(Color.RED, "Дворец культуры", Duration.ofSeconds(266));

        metro.createLine(Color.BLUE);
        metro.createFirstStation(Color.BLUE, "Пацанская");
        metro.createFinallyStation(Color.BLUE, "Улица Кирова", Duration.ofSeconds(90));
        metro.createFinallyStation(Color.BLUE, "Тяжмаш", Duration.ofSeconds(107), "Пермь 1");
        metro.createFinallyStation(Color.BLUE, "Нижнекамская", Duration.ofSeconds(209));
        metro.createFinallyStation(Color.BLUE, "Соборная", Duration.ofSeconds(108));

        GregorianCalendar date1 = new GregorianCalendar(2023, Calendar.DECEMBER, 10);
        GregorianCalendar date2 = new GregorianCalendar(2023, Calendar.DECEMBER, 11);

        station01.sellTicket(date1, "Спортивная", "Соборная");
        station01.sellTicket(date1, "Молодежная", "Соборная");
        station01.sellTicket(date2, "Пацанская", "Соборная");

        System.out.println(station01.getTicketOffice());
    }

}
