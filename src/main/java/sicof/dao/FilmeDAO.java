package sicof.dao;

import sicof.connection.DBConnection;
import sicof.model.Filme;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FilmeDAO extends DBConnection {
    private PreparedStatement preparedStatement = null;

    public boolean insert(Filme filme) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO movies (title, release_date, category_id) VALUES (?, ?, ?)";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, filme.getTitle());
            this.preparedStatement.setDate(2, new Date(filme.getReleaseDate().getTime()));
            this.preparedStatement.setInt(3, filme.getCategory().getId());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Filme filme) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "UPDATE movies SET title = ?, release_date = ?, category_id = ? WHERE id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, filme.getTitle());
            this.preparedStatement.setDate(2, (Date) new Date(filme.getReleaseDate().getTime()));
            this.preparedStatement.setInt(3, filme.getCategory().getId());
            this.preparedStatement.setInt(4, filme.getId());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Filme filme) {
        try {
            AtorFilmesDAO atorFilmesDAO = new AtorFilmesDAO();
            atorFilmesDAO.deleteByMovieId(filme.getId());

            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM movies WHERE id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, filme.getId());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Filme> getAll() {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM movies";
            this.preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            ArrayList<Filme> filmes = new ArrayList<>();
            while (resultSet.next()) {
                Filme filme = new Filme();
                filme.setId(resultSet.getInt("id"));
                filme.setTitle(resultSet.getString("title"));
                filme.setReleaseDate(resultSet.getDate("release_date"));
                filme.setCategory(new CategoriaDAO().getById(resultSet.getInt("category_id")));
                filme.setActors(new AtorFilmesDAO().getByMovieId(filme.getId()));
                filmes.add(filme);
            }

            connection.commit();
            connection.close();

            return filmes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Filme> search(String title) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM movies WHERE title LIKE ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setString(1, "%" + title + "%");
            ResultSet resultSet = this.preparedStatement.executeQuery();

            ArrayList<Filme> filmes = new ArrayList<>();
            while (resultSet.next()) {
                Filme filme = new Filme();
                filme.setId(resultSet.getInt("id"));
                filme.setTitle(resultSet.getString("title"));
                filme.setReleaseDate(resultSet.getDate("release_date"));
                filme.setCategory(new CategoriaDAO().getById(resultSet.getInt("category_id")));
                filme.setActors(new AtorFilmesDAO().getByMovieId(filme.getId()));
                filmes.add(filme);
            }

            connection.commit();
            connection.close();

            return filmes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Filme getLast() {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM movies ORDER BY id DESC LIMIT 1";
            this.preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            Filme filme = new Filme();
            while (resultSet.next()) {
                filme.setId(resultSet.getInt("id"));
                filme.setTitle(resultSet.getString("title"));
                filme.setReleaseDate(resultSet.getDate("release_date"));
                filme.setCategory(new CategoriaDAO().getById(resultSet.getInt("category_id")));
            }

            connection.commit();
            connection.close();

            return filme;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Filme getById(int id) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM movies WHERE id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, id);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            Filme filme = new Filme();
            while (resultSet.next()) {
                filme.setId(resultSet.getInt("id"));
                filme.setTitle(resultSet.getString("title"));
                filme.setReleaseDate(resultSet.getDate("release_date"));
                filme.setCategory(new CategoriaDAO().getById(resultSet.getInt("category_id")));
            }

            AtorFilmesDAO atorFilmesDAO = new AtorFilmesDAO();
            filme.setActors(atorFilmesDAO.getByMovieId(filme.getId()));

            connection.commit();
            connection.close();

            return filme;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean existsWithCategory(int categoryId) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM movies WHERE category_id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            boolean exists = resultSet.next();

            connection.commit();
            connection.close();

            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
