package sicof.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public DBConnection() {

    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;

        connection = DriverManager.getConnection("jdbc:mysql://localhost/sicof?" + "user=admin&password=admin");
        // Configura a conexão com o banco de dados

        return connection;
    }
}
