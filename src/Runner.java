import exceptions.ImpossibleCreateLineException;
import exceptions.ImpossibleCreateStationException;
import java.time.Duration;
import metro.Color;
import metro.Metro;


public class Runner {
    public static void main(String[] args) throws ImpossibleCreateLineException, ImpossibleCreateStationException {
        Metro metro = new Metro("Пермь");
        addStationMetro(metro);
        //addStationMetroTest(metro);
        System.out.println(metro);
    }

    public static void addStationMetro(Metro metro) throws ImpossibleCreateLineException,
            ImpossibleCreateStationException {
        metro.createLine(Color.RED);
        metro.createFirstStation(Color.RED, "Спортивная");
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
    }

}
