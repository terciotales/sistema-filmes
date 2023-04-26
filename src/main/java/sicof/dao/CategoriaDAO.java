package sicof.dao;

import sicof.connection.DBConnection;
import sicof.model.Ator;
import sicof.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CategoriaDAO extends DBConnection {
    private PreparedStatement preparedStatement = null;

    public boolean insert(Categoria categoria) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO categories (name) VALUES (?)";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, categoria.getName());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Categoria categoria) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "UPDATE categories SET name = ? WHERE id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, categoria.getName());
            this.preparedStatement.setInt(2, categoria.getId());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Categoria categoria) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM categories WHERE id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, categoria.getId());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Categoria> getAll() {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM categories ORDER BY name";
            this.preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            ArrayList<Categoria> categorias = new ArrayList<>();
            while (resultSet.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(resultSet.getInt("id"));
                categoria.setName(resultSet.getString("name"));
                categorias.add(categoria);
            }

            connection.commit();
            connection.close();

            return categorias;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Categoria getById(int id) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM categories WHERE id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, id);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            Categoria categoria = new Categoria();
            if (resultSet.next()) {
                categoria.setId(resultSet.getInt("id"));
                categoria.setName(resultSet.getString("name"));
            }

            connection.commit();
            connection.close();

            return categoria;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Categoria getByName(String name) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM categories WHERE LOWER(name) = LOWER(?)";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, name);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            Categoria categoria = null;
            if (resultSet.next()) {
                categoria = new Categoria();
                categoria.setId(resultSet.getInt("id"));
                categoria.setName(resultSet.getString("name"));
            }

            connection.commit();
            connection.close();

            return categoria;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Categoria> search(String name) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM categories WHERE name LIKE ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = this.preparedStatement.executeQuery();

            ArrayList<Categoria> categorias = new ArrayList<>();
            while (resultSet.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(resultSet.getInt("id"));
                categoria.setName(resultSet.getString("name"));
                categorias.add(categoria);
            }

            connection.commit();
            connection.close();

            return categorias;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
