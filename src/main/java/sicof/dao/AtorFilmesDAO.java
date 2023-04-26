package sicof.dao;

import sicof.connection.DBConnection;
import sicof.model.Ator;
import sicof.model.AtorFilmes;
import sicof.model.Filme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AtorFilmesDAO extends DBConnection {
    private PreparedStatement preparedStatement = null;

    public boolean insert(AtorFilmes atorFilmes) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO actor_movies (actor_id, movie_id) VALUES (?, ?)";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, atorFilmes.getAtor().getId());
            this.preparedStatement.setInt(2, atorFilmes.getFilme().getId());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(AtorFilmes atorFilmes) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM actor_movies WHERE actor_id = ? AND movie_id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, atorFilmes.getAtor().getId());
            this.preparedStatement.setInt(2, atorFilmes.getFilme().getId());
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByMovieId(int movieId) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM actor_movies WHERE movie_id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, movieId);
            this.preparedStatement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public AtorFilmes get(Ator ator, Filme filme) {
        AtorFilmes atorFilmes = new AtorFilmes();

        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM actor_movies WHERE actor_id = ? AND movie_id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, ator.getId());
            this.preparedStatement.setInt(2, filme.getId());
            ResultSet resultSet = this.preparedStatement.executeQuery();

            if (resultSet.next()) {
                atorFilmes.setAtor(ator);
                atorFilmes.setFilme(filme);
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return atorFilmes;
    }

    public boolean exists(int atorId, int filmeId) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM actor_movies WHERE actor_id = ? AND movie_id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, atorId);
            this.preparedStatement.setInt(2, filmeId);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Ator> getByMovieId(int movieId) {
        ArrayList<Ator> atores = new ArrayList<>();

        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT actors.id, actors.name FROM actors INNER JOIN actor_movies ON actors.id = actor_movies.actor_id WHERE actor_movies.movie_id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, movieId);
            ResultSet resultSet = this.preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ator ator = new Ator();
                ator.setId(resultSet.getInt("id"));
                ator.setName(resultSet.getString("name"));

                atores.add(ator);
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return atores;
    }

    public boolean hasFilmes(Ator ator) {
        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT * FROM actor_movies WHERE actor_id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, ator.getId());
            ResultSet resultSet = this.preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int countAtorFilmes(Ator ator) {
        int count = 0;

        try {
            Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT COUNT(*) FROM actor_movies WHERE actor_id = ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, ator.getId());
            ResultSet resultSet = this.preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}
