import exceptions.ChangeLineException;
import exceptions.ImpossibleBuildRoute;
import exceptions.ImpossibleCreateLineException;
import exceptions.ImpossibleCreateStationException;
import java.time.Duration;
import metro.Color;
import metro.Metro;
import metro.Station;


public class Runner {
    public static void main(String[] args) throws ImpossibleCreateLineException, ImpossibleCreateStationException,
            ChangeLineException, ImpossibleBuildRoute {
        Metro metro = new Metro("Пермь");
        addStationMetro(metro);
        //addStationMetroTest(metro);
        System.out.println(metro);
        System.out.println(metro.findStationChange(Color.RED, Color.BLUE));

    }

    public static void addStationMetro(Metro metro) throws ImpossibleCreateLineException,
            ImpossibleCreateStationException, ChangeLineException, ImpossibleBuildRoute {
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

        metro.createLine(Color.ORANGE);
        metro.createFirstStation(Color.ORANGE, "Царицыно");
        metro.createFinallyStation(Color.ORANGE, "Подольск", Duration.ofSeconds(90));

        String startStation = "Соборная";
        String finishStation = "Подольск";

        int sum = metro.sumRuns(startStation, finishStation);
        System.out.println(sum);
        System.out.println();
        System.out.println();


        /*int sum = metro.sumRunsOnOneLine(startStation, finishStation);
        int sum2 = metro.sumRunsOnOneLine(finishStation, startStation);
        System.out.println(sum);
        System.out.println(sum2);*/


    }

}
