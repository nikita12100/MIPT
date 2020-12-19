import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, ParseException {

        DataBase.createAllTables("FightsData");

        DataBase.selectCityWithSomeAirports("FightsData");
        DataBase.selectCityAndCancelFlight("FightsData");
        DataBase.selectShortestFlight("FightsData");
        DataBase.selectMonthAndCanceledFlight("FightsData");
        DataBase.selectMoscowInOutFlights("FightsData");
        DataBase.removeAircraftFlightsAndTickets("FightsData", "Boeing 777-300");   // code 773
        DataBase.cancelFlights("FightsData", "2017-06-15 00:00:00+03", "2018-09-15 00:00:00+03");  // yyyy-MM-ww HH:mm:ss
        List<String> ticketData = DataBase.createTicket("FightsData", "PG0134", "2A", "AB0123", "NIKITA", "+7901");

    }
}
