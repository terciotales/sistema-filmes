package sicof.dao;

import sicof.connection.DBConnection;
import sicof.model.Ator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AtorDAO extends DBConnection {
    private PreparedStatement preparedStatement = null;

    public boolean insert(Ator ator) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO actors (name) VALUES (?)";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, ator.getName());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Ator ator) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "UPDATE actors SET name = ? WHERE id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, ator.getName());
            this.preparedStatement.setInt(2, ator.getId());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Ator ator) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM actors WHERE id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, ator.getId());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Ator get(int id) {
        Ator ator = null;

        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM actors WHERE id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, id);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            if (resultSet.next()) {
                ator = new Ator(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ator;
    }

    public Ator get(String name) {
        Ator ator = null;

        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM actors WHERE LOWER(name) = LOWER(?)";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, name);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            if (resultSet.next()) {
                ator = new Ator(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ator;
    }

    public ArrayList<Ator> getAll() {
        ArrayList<Ator> atores = new ArrayList<>();

        try {
            Connection connection = new DBConnection().getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM actors ORDER BY name";
            this.preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            while (resultSet.next()) {
                atores.add(new Ator(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return atores;
    }

    public ArrayList<Ator> search(String name) {
        ArrayList<Ator> atores = new ArrayList<>();

        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM actors WHERE name LIKE ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = this.preparedStatement.executeQuery();

            while (resultSet.next()) {
                atores.add(new Ator(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return atores;
    }

    public Ator getById(int id) {
        Ator ator = null;

        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM actors WHERE id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, id);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            if (resultSet.next()) {
                ator = new Ator(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ator;
    }
}
