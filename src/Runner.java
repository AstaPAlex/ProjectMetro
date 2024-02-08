import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import metro.Color;
import metro.Metro;
import metro.Station;


public class Runner {
    private static Station station1;
    private static Station station2;

    public static void main(String[] args) {
        Metro metro = new Metro("Пермь");
        addStationMetro(metro);
        System.out.println(metro);
        sellTickets(metro);
        metro.printReport();

    }

    public static void addStationMetro(Metro metro) {
        metro.createLine(Color.RED);

        station1 = metro.createFirstStation(Color.RED, "Спортивная");
        station2 = metro.createFinallyStation(Color.RED, "Медведковская", Duration.ofSeconds(141));
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
    }

    public static void sellTickets(Metro metro) {
        LocalDateTime date1 = LocalDateTime.of(2023, Month.DECEMBER, 22, 15, 10);
        LocalDateTime date2 = LocalDateTime.of(2023, Month.DECEMBER, 23, 16, 20);
        LocalDateTime date3 = LocalDateTime.of(2023, Month.DECEMBER, 24, 17, 22);

        station1.sellTicket(date1, "Спортивная", "Соборная");
        station1.sellTicket(date2, "Спортивная", "Пермь 1");
        station1.sellTicket(date2, "Пацанская", "Соборная");
        station1.sellTicket(date3, "Молодежная", "Нижнекамская");
        station2.sellTicket(date3, "Нижнекамская", "Молодежная");

        station1.sellSeasonTicket(LocalDate.of(2023, Month.DECEMBER, 22));
        station1.sellSeasonTicket(LocalDate.of(2023, Month.DECEMBER, 23));

        boolean result1 = metro.checkValidation("a0000", LocalDate.of(2024, Month.JANUARY, 22));
        boolean result2 = metro.checkValidation("a0001", LocalDate.of(2024, Month.JANUARY, 22));
        System.out.println("Действиетелен проездной a0000:" + result1);
        System.out.println("Действиетелен проездной a0001:" + result2);

        station1.extendSeasonTicket("a0000", LocalDate.of(2024, Month.FEBRUARY, 25));
        boolean result3 = metro.checkValidation("a0000", LocalDate.of(2024, Month.FEBRUARY, 26));

        System.out.println("Действиетелен проездной a0000:" + result3);
    }

}
