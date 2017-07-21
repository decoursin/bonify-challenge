package bonify;

import com.jolbox.bonecp.BoneCPDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/bonify";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "";

    public static DataSource DATA_SOURCE = getDataSource();

    public static DataSource getDataSource() {
        BoneCPDataSource ds = new BoneCPDataSource();  // create a new datasource object
        ds.setDriverClass(JDBC_DRIVER);
        ds.setJdbcUrl(JDBC_URL);        // set the JDBC url
        ds.setUsername(USERNAME);                // set the username
        ds.setPassword(PASSWORD);                // set the password

        return ds;
    }
}
