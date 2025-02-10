package vttp2023.batch3.assessment.paf.bookings.Utils;

public class SqlQueries 
{
    public static final String CHECK_VACANCY = """
            SELECT * FROM acc_occupancy WHERE acc_id = ?;
            """;

    public static final String INSERT_RESERVATIONS = """
            INSERT INTO reservations (resv_id, name, email, acc_id, arrival_date, duration) 
                VALUES(?, ?, ?, ?, ?, ?);
            """;

    public static final String UPDATE_VACANCY = """
            UPDATE acc_occupancy 
                SET vacancy = ? 
                where acc_id = ?;
            """;
}
