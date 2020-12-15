import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.dbcp2.BasicDataSource;


public class DataBase {

    private static Connection connDB;
    private static Statement statmt;
    private static ResultSet resSet;
    private static BasicDataSource dataSource;
    private static String dbTickets = "tickets";
    private static String dbTicketFlights = "ticket_flights";
    private static String dbSeats = "seats";
    private static String dbFlights = "flights";
    private static String dbBookings = "bookings";
    private static String dbAircrafts = "aircrafts";
    private static String dbAirports = "airports";
    private static String dbBoardingPasses = "boarding_passes";

    private static HashMap<String, List<String>> CityAndAirports;

    /**
     * Create all database tables
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static void createAllTables(String dbName) throws SQLException, ClassNotFoundException, IOException {
        connDB(dbName);
        createDB("'ticket_no' CHAR(13)," +
                "'book_ref' char(6)," +
                "'passenger_id' varchar(20)," +
                "'passenger_name' text," +
                "'contact_data' jsonb", dbTickets);
        fillFromCSV("data_csv/tickets.csv", dbTickets);

        createDB("'ticket_no' CHAR(13)," +
                "'flight_id' INT," +
                "'fare_conditions' varchar(10)," +
                "'amount' numeric(10, 2)", dbTicketFlights);
        fillFromCSV("data_csv/ticket_flights.csv", dbTicketFlights);

        createDB("'aircraft_code' CHAR(3)," +
                "'seat_no' varchar(4)," +
                "'fare_conditions' varchar(10)", dbSeats);
        fillFromCSV("data_csv/seats.csv", dbSeats);

        createDB("'flight_id' serial," +
                "'flight_no' char(6)," +
                "'scheduled_departure' timestamptz ," +
                "'scheduled_arrival' timestamptz ," +
                "'departure_airport' char(3)," +
                "'arrival_airport' char(3)," +
                "'status' varchar(20)," +
                "'aircraft_code' char(3)," +
                "'actual_departure' timestamptz ," +
                "'actual_arrival' timestamptz", dbFlights);
        fillFromCSV("data_csv/flights.csv", dbFlights);

        createDB("'book_ref' CHAR(6)," +
                "'book_date' timestamptz," +
                "'total_amount' numeric(10,2)", dbBookings);
        fillFromCSV("data_csv/bookings.csv", dbBookings);

        createDB("'aircraft_code' CHAR(3)," +
                " 'model' JSONB," +
                " 'range' INT", dbAircrafts);
        fillFromCSV("data_csv/aircrafts.csv", dbAircrafts);

        createDB("'airport_code' CHAR(3)," +
                "'airport_name' JSONB," +
                "'city' JSONB," +
                "'coordinates' POINT," +
                "'timezone' TEXT", dbAirports);
        fillFromCSV("data_csv/airports_data.csv", dbAirports);

        createDB("'ticket_no' CHAR(13)," +
                "'flight_id' INT," +
                "'boarding_no' INT," +
                "'seat_no' varchar(4)", dbBoardingPasses);
        fillFromCSV("data_csv/boarding_passes.csv", dbBoardingPasses);
        closeDB();
    }

    /**
     * Connect one database
     *
     * @param dbName -- name of database
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void connDB(String dbName) throws ClassNotFoundException, SQLException {
        try {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.sqlite.JDBC");
            dataSource.setUrl("jdbc:sqlite:db/" + dbName + ".s3db");
            dataSource.setMinIdle(5);
            dataSource.setMaxIdle(10);
            dataSource.setMaxOpenPreparedStatements(100);

            connDB = dataSource.getConnection();
            statmt = connDB.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("База " + dbName + " подключена!");
    }

    /**
     * Create table
     *
     * @param tableHead -- "'aircraft_code' CHAR(3) PRIMARY KEY, 'model' JSONB, 'range' INT"
     * @param dbName    -- name of database
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void createDB(String tableHead, String dbName) throws ClassNotFoundException, SQLException {
        statmt.execute("CREATE TABLE if not exists '" + dbName + "' (" + tableHead + ");");

        System.out.println("Таблица " + dbName + " создана или уже существует.");
    }

    /**
     * Insert into table values
     *
     * @param values -- values, example="'0', '{}', '125'"
     * @param dbName -- table
     * @throws SQLException
     */
    private static void write(String values, String dbName) throws SQLException {
        switch (dbName) {
            case "aircrafts":
                statmt.execute("INSERT INTO '" + dbName + "' ('aircraft_code', 'model', 'range') VALUES " + values);
                break;
            case "airports":
                statmt.execute("INSERT INTO '" + dbName + "' ('airport_code', 'airport_name', 'city', 'coordinates', 'timezone') VALUES " + values);
                break;
            case "boarding_passes":
                statmt.execute("INSERT INTO '" + dbName + "' ('ticket_no', 'flight_id', 'boarding_no', 'seat_no') VALUES " + values);
                break;
            case "bookings":
                statmt.execute("INSERT INTO '" + dbName + "' ('book_ref', 'book_date', 'total_amount') VALUES " + values);
                break;
            case "flights":
                statmt.execute("INSERT INTO '" + dbName + "' ('flight_id', 'flight_no', 'scheduled_departure', 'scheduled_arrival'" +
                        ",'departure_airport' ,'arrival_airport', 'status', 'aircraft_code', 'actual_departure', 'actual_arrival') VALUES " + values);
                break;
            case "seats":
                statmt.execute("INSERT INTO '" + dbName + "' ('aircraft_code', 'seat_no', 'fare_conditions') VALUES " + values);
                break;
            case "ticket_flights":
                statmt.execute("INSERT INTO '" + dbName + "' ('ticket_no', 'flight_id', 'fare_conditions', 'amount') VALUES " + values);
                break;
            case "tickets":
                statmt.execute("INSERT INTO '" + dbName + "' ('ticket_no', 'book_ref', 'passenger_id', 'passenger_name', 'contact_data') VALUES " + values);
                break;
            default:
                break;
        }

        System.out.println("Таблица " + dbName + " заполнена");
    }

    /**
     * Parse csv line to table values
     *
     * @param line   example="0, {}, 125"
     * @param dbName -- table
     * @return example="'0', '{}', '125'"
     */
    private static String parseHeadCSV(String line, String dbName) {
        switch (dbName) {
            case "aircrafts":
                String[] splitString = line.split(","); // split by the separator

                StringBuilder json = new StringBuilder();
                for (int i = 1; i < splitString.length - 1; ++i) {
                    json.append(splitString[i]);
                }

                return "'" + splitString[0] + "', '" + json + "', '" + splitString[splitString.length - 1] + "'";
            case "airports":
                String[] sp2 = line.split(","); // split by the separator
                return "'" + sp2[0] + "', '" + sp2[1] + sp2[2] + "', '" + sp2[3] + sp2[4] + "', '" + sp2[5] + sp2[6] + "', '" + sp2[7] + "'";
            case "boarding_passes":
                String[] sp3 = line.split(","); // split by the separator
                return "'" + sp3[0] + "', '" + sp3[1] + "', '" + sp3[2] + "', '" + sp3[3] + "'";
            case "bookings":
                String[] sp4 = line.split(","); // split by the separator
                return "'" + sp4[0] + "', '" + sp4[1] + "', '" + sp4[2] + "'";
            case "flights":
                String[] sp5 = line.split(","); // split by the separator
                String actual_departure = "";
                if (sp5.length == 9) {
                    actual_departure = sp5[8];
                }
                String actual_arrival = "";
                if (sp5.length == 10) {
                    actual_arrival = sp5[9];
                }
                return "'" + sp5[0] + "', '" + sp5[1] + "', '" + sp5[2] + "', '" + sp5[3] + "', '" + sp5[4] +
                        "', '" + sp5[5] + "', '" + sp5[6] + "', '" + sp5[7] + "', '" + actual_departure + "', '" + actual_arrival + "'";
            case "seats":
                String[] sp6 = line.split(",");
                return "'" + sp6[0] + "', '" + sp6[1] + "', '" + sp6[2] + "'";
            case "ticket_flights":
                String[] sp7 = line.split(",");
                return "'" + sp7[0] + "', '" + sp7[1] + "', '" + sp7[2] + "', '" + sp7[3] + "'";
            case "tickets":
                String[] sp8 = line.split(",");
                return "'" + sp8[0] + "', '" + sp8[1] + "', '" + sp8[2] + "', '" + sp8[3] + "', '" + sp8[4] + "'";
            default:
                return "Error parse";
        }
    }

    /**
     * Fill table with data in CSV file
     *
     * @param CSVPath -- path of CSV file
     * @param dbName  -- table name
     * @throws IOException
     * @throws SQLException
     */
    private static void fillFromCSV(String CSVPath, String dbName) throws IOException, SQLException {
        BufferedReader reader = null;
        try {
            File file = new File(CSVPath); // path to "MyFile.csv"
            reader = new BufferedReader(new FileReader(file));

            String line;
            StringBuilder values = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                values.append("(").append(parseHeadCSV(line, dbName)).append("),");
            }
            values = new StringBuilder(values.substring(0, values.length() - 1));
            values.append(";");
            write(values.toString(), dbName);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    ---------------------------------------- QUERIES -----------------------------------

    /**
     * Fill HashMap<city, airports>
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static void fillCityAndAirports(String dbName) throws SQLException, ClassNotFoundException {
        connDB(dbName);
        resSet = statmt.executeQuery("SELECT airport_name, city FROM " + dbAirports);

        CityAndAirports = new HashMap<>();
        while (resSet.next()) {
            String airport_name = resSet.getString("airport_name");
            String city = resSet.getString("city");

            String airport_name_ = airport_name.split("\"{2}")[3];
            String city_ = city.split("\"{2}")[3];

            if (CityAndAirports.get(city_) == null) {
                List<String> l = new ArrayList<>();
                l.add(airport_name_);
                CityAndAirports.put(city_, l);
            } else if (!CityAndAirports.get(city_).contains(airport_name_)) {
                CityAndAirports.get(city_).add(airport_name_);
            }
        }

        closeDB();
    }

    /**
     * select cities with some airports
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void selectCityWithSomeAirports(String dbName) throws SQLException, ClassNotFoundException {
        fillCityAndAirports(dbName);

        System.out.println("Города, в которых несколько аэропортов:");
        for (String city : CityAndAirports.keySet()) {
            if (CityAndAirports.get(city).size() > 1) {
                System.out.println(city + " : " + CityAndAirports.get(city));
            }
        }
    }

    /**
     * get city by airport
     *
     * @param airport -- airport
     * @return -- city name
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static String getCityByAirport(String airport) throws SQLException, ClassNotFoundException {
        String resCity = "None";

        for (String city : CityAndAirports.keySet()) {
            if (CityAndAirports.get(city).contains(airport)) {
                return city;
            }
        }
        return resCity;
    }

    /**
     * selectCityAndCancelFlight
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void selectCityAndCancelFlight(String dbName) throws SQLException, ClassNotFoundException {
        connDB(dbName);
        resSet = statmt.executeQuery("SELECT departure_airport, status FROM " + dbFlights);

        HashMap<String, Integer> CityAndCancelFlight = new HashMap<>();
        while (resSet.next()) {
            String departure_airport = resSet.getString("departure_airport");
            String status = resSet.getString("status");

            if (status.equals("Cancelled")) {
                if (CityAndCancelFlight.containsKey(departure_airport)) {
                    CityAndCancelFlight.put(departure_airport, CityAndCancelFlight.get(departure_airport) + 1);
                } else {
                    CityAndCancelFlight.put(departure_airport, 1);
                }
            }
        }

        System.out.println("Города, из которых чаще всего отменяли рейсы:");
        CityAndCancelFlight.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(System.out::println);

        closeDB();
    }

    /**
     * selectShortestFlight
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws ParseException
     */
    public static void selectShortestFlight(String dbName) throws SQLException, ClassNotFoundException, ParseException {
        connDB(dbName);
        resSet = statmt.executeQuery("SELECT scheduled_departure, scheduled_arrival, departure_airport, arrival_airport  FROM " + dbFlights);

        HashMap<String, Long> CityAndCancelFlight = new HashMap<>();
        HashMap<String, String> CityFromTo = new HashMap<>();
        while (resSet.next()) {
            String scheduled_departure = resSet.getString("scheduled_departure");
            String scheduled_arrival = resSet.getString("scheduled_arrival");
            String departure_airport = resSet.getString("departure_airport");
            String arrival_airport = resSet.getString("arrival_airport");

            DateFormat format = new SimpleDateFormat("yyyy-MM-ww HH:mm:ss");
            Date date_departure = format.parse(scheduled_departure);
            Date date_arrival = format.parse(scheduled_arrival);
            long seconds = (date_arrival.getTime() - date_departure.getTime()) / 1000;
            if(seconds < 0){
                continue;
            }

            if (CityAndCancelFlight.containsKey(departure_airport)) {
                if (CityAndCancelFlight.get(departure_airport) > seconds) {
                    CityAndCancelFlight.replace(departure_airport, seconds);
                    CityFromTo.replace(departure_airport, arrival_airport);
                }
            } else {
                CityAndCancelFlight.put(departure_airport, seconds);
                CityFromTo.put(departure_airport, arrival_airport);
            }
        }

        System.out.println("Самый короткий маршрут:");
        CityAndCancelFlight.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(val -> System.out.println("From " + val.getKey() + " to " + CityFromTo.get(val.getKey()) + " for " + val.getValue()));

        closeDB();
    }

    /**
     * selectMonthAndCanceledFlight
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws ParseException
     */
    public static void selectMonthAndCanceledFlight(String dbName) throws SQLException, ClassNotFoundException, ParseException {
        connDB(dbName);
        resSet = statmt.executeQuery("SELECT scheduled_departure, status FROM " + dbFlights);

        HashMap<Integer, Integer> CityAndCancelFlight = new HashMap<>();
        while (resSet.next()) {
            String scheduled_departure = resSet.getString("scheduled_departure");
            String status = resSet.getString("status");

            DateFormat format = new SimpleDateFormat("yyyy-MM-ww HH:mm:ss");
            Date date_departure = format.parse(scheduled_departure);

            if (status.equals("Cancelled")) {
                if (CityAndCancelFlight.containsKey(date_departure.getMonth())) {
                    CityAndCancelFlight.put(date_departure.getMonth(), CityAndCancelFlight.get(date_departure.getMonth()) + 1);
                } else {
                    CityAndCancelFlight.put(date_departure.getMonth(), 1);
                }
            }
        }

        System.out.println("Количество отмен рейсов по месяцам:");
        System.out.println("month=count");
        CityAndCancelFlight.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(System.out::println);

        closeDB();
    }

    /**
     * selectMoscowInOutFlights
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws ParseException
     */
    public static void selectMoscowInOutFlights(String dbName) throws SQLException, ClassNotFoundException, ParseException {
        connDB(dbName);
        resSet = statmt.executeQuery("SELECT scheduled_departure, departure_airport, arrival_airport FROM " + dbFlights);

        int[] inFlight = new int[8];
        int[] outFlight = new int[8];
        while (resSet.next()) {
            String scheduled_departure = resSet.getString("scheduled_departure");
            String departure_airport = resSet.getString("departure_airport");
            String arrival_airport = resSet.getString("arrival_airport");

            DateFormat format = new SimpleDateFormat("yyyy-MM-ww HH:mm:ss");
            Date date_departure = format.parse(scheduled_departure);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date_departure);
            int curr_day = calendar.get(Calendar.DAY_OF_WEEK); //1=sunday, 2=monday, 3=Calendar.WEDNESDAY...

            if (departure_airport.equals("SVO") || departure_airport.equals("DME")
                    || departure_airport.equals("ZIA") || departure_airport.equals("VKO")) {
                ++outFlight[curr_day];
            }
            if (arrival_airport.equals("SVO") || arrival_airport.equals("DME")
                    || arrival_airport.equals("ZIA") || arrival_airport.equals("VKO")) {
                ++inFlight[curr_day];
            }

        }

        System.out.println("Количество отмен рейсов в Мосвкве по месяцам:");
        System.out.println("In flights:");
        String[] week = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 1; i < 8; ++i) {
            System.out.println(week[i - 1] + ":" + inFlight[i]);
        }
        System.out.println("Out flights:");
        for (int i = 1; i < 8; ++i) {
            System.out.println(week[i - 1] + ":" + outFlight[i]);
        }
        closeDB();
    }

    /**
     * getAircraftCode
     *
     * @param aircraftModel -- model of plane
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static String getAircraftCode(String aircraftModel) throws SQLException, ClassNotFoundException {
        resSet = statmt.executeQuery("SELECT aircraft_code, model FROM " + dbAircrafts);
        while (resSet.next()) {
            String aircraft_code = resSet.getString("aircraft_code");
            String model = resSet.getString("model");
            String[] splitString = model.split("\"{2}"); // split by the separator
            model = splitString[3];

            if (model.equals(aircraftModel)) {
                return aircraft_code;
            }
        }
        return "None";
    }

    /**
     * removeAircraftFlightsAndTickets
     *
     * @param aircraftModel -- aircraft model
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws ParseException
     */
    public static void removeAircraftFlightsAndTickets(String dbName, String aircraftModel) throws SQLException, ClassNotFoundException, ParseException {
        connDB(dbName);
        String aircraftCode = getAircraftCode(aircraftModel);
        PreparedStatement stmt = connDB.prepareStatement("DELETE FROM " + dbFlights + " WHERE aircraft_code = ?");
        stmt.setString(1, aircraftCode);
        stmt.execute();

//        stmt = connDB.prepareStatement("DELETE FROM " + dbTickets + " WHERE ticket_no IN " +
//                "((SELECT ticket_no FROM " + dbTicketFlights + " WHERE flight_id IN " +
//                "(SELECT flight_id FROM " + dbFlights + " WHERE aircraft_code = ?))");
//        stmt.setString(1, aircraftCode);
//        stmt.execute();

        System.out.println("Рейсы " + aircraftModel + " удалены.");

        closeDB();
    }

    /**
     * cancelFlights
     *
     * @param from_ -- start period
     * @param to_   -- end period
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws ParseException
     */
    public static void cancelFlights(String dbName, String from_, String to_) throws SQLException, ClassNotFoundException, ParseException {
        connDB(dbName);
        resSet = statmt.executeQuery("SELECT flight_id, scheduled_departure, departure_airport, arrival_airport FROM " + dbFlights);

        DateFormat format = new SimpleDateFormat("yyyy-MM-ww HH:mm:ss");
        Date from = format.parse(from_);
        Date to = format.parse(to_);
        StringBuilder flightIds = new StringBuilder();
        flightIds.append("(");
        while (resSet.next()) {
            String flight_id = resSet.getString("flight_id");
            String scheduled_departure = resSet.getString("scheduled_departure");
            String departure_airport = resSet.getString("departure_airport");
            String arrival_airport = resSet.getString("arrival_airport");

            Date date_departure = format.parse(scheduled_departure);
            if ((date_departure.after(from) && date_departure.before(to)) &&
                    ((departure_airport.equals("DME") || departure_airport.equals("SVO") || departure_airport.equals("ZIA")) ||
                            (arrival_airport.equals("DME") || arrival_airport.equals("SVO") || arrival_airport.equals("ZIA")))) {
                flightIds.append(flight_id);
                flightIds.append(", ");
            }
        }
        flightIds = new StringBuilder(flightIds.substring(0, flightIds.length() - 2));
        flightIds.append(")");
        String values = flightIds.toString();

        PreparedStatement stmt = connDB.prepareStatement("UPDATE " + dbFlights + " SET status = 'Cancelled' WHERE flight_id = ?");
        stmt.setString(1, values);
        stmt.execute();

        System.out.println("Все рейсы в Москву и из Москвы в период с " + from_ + " по " + to_ + " были отменены.");

        resSet = statmt.executeQuery("SELECT amount FROM " + dbTicketFlights + " WHERE flight_id IN " + values);
        long wasteSum = 0;
        while (resSet.next()) {
            int amount = resSet.getInt("amount");
            wasteSum += amount;
        }
        closeDB();

        System.out.println("Убыток за этот период составил " + wasteSum);
    }

    /**
     * Create ticket
     *
     * @param flight_no_     -- number of flight
     * @param seat_no_       -- seat number
     * @param passenger_id   -- passenger id
     * @param passenger_name -- name
     * @param contact_data   -- data
     * @return -- <ticket number, book number>
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<String> createTicket(String dbName, String flight_no_, String seat_no_, String passenger_id, String passenger_name, String contact_data) throws SQLException, ClassNotFoundException {
        connDB(dbName);

        PreparedStatement stmt = connDB.prepareStatement("SELECT aircraft_code, seat_no FROM " + dbSeats + " WHERE seat_no = ? AND aircraft_code IN " +
                "(SELECT aircraft_code FROM " + dbFlights + " WHERE flight_no = ?)");
        stmt.setString(1, seat_no_);
        stmt.setString(2, flight_no_);
        resSet = stmt.executeQuery();
        if (resSet.next()) {
            statmt.execute("INSERT INTO '" + dbTickets + "' ('ticket_no', 'book_ref', 'passenger_id', 'passenger_name', 'contact_data') VALUES ('" +
                    passenger_id + flight_no_ + "', '" + passenger_id + seat_no_ + "', '" + passenger_id + "', '" + passenger_name + "', '" + contact_data + "'); ");
            closeDB();
            System.out.println("Билет " + passenger_id + flight_no_ + " создан, номер бронирования: " + passenger_id + seat_no_);
            return Arrays.asList(passenger_id + flight_no_, passenger_id + seat_no_);
        }
        closeDB();
        System.out.println("Рейс или билет не найден.");
        return Arrays.asList("None", "None");
    }

    /**
     * close DataBase
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void closeDB() throws ClassNotFoundException, SQLException {
        statmt.close();
        connDB.close();
        if (resSet != null) {
            resSet.close();
        }

        System.out.println("Соединения  закрыты");
        System.out.println();
    }

}
