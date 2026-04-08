package org.example.services;

import org.example.entities.MatchGame;
import org.example.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceMatchGame implements IService<MatchGame> {
    private Connection conn;

    public ServiceMatchGame() {
        conn = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(MatchGame matchGame) throws SQLException {
        String sql = "INSERT INTO match_game(date_match, score_team1, score_team2, statut, equipe1_id, equipe2_id, tournoi_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, matchGame.getDateMatch());
            ps.setObject(2, matchGame.getScoreTeam1());
            ps.setObject(3, matchGame.getScoreTeam2());
            ps.setString(4, matchGame.getStatut());
            ps.setInt(5, matchGame.getEquipe1Id());
            ps.setInt(6, matchGame.getEquipe2Id());
            ps.setInt(7, matchGame.getTournoiId());
            ps.executeUpdate();
        }
    }

    @Override
    public void modifier(MatchGame matchGame) throws SQLException {
        String sql = "UPDATE match_game SET date_match=?, score_team1=?, score_team2=?, statut=?, equipe1_id=?, equipe2_id=?, tournoi_id=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, matchGame.getDateMatch());
            ps.setObject(2, matchGame.getScoreTeam1());
            ps.setObject(3, matchGame.getScoreTeam2());
            ps.setString(4, matchGame.getStatut());
            ps.setInt(5, matchGame.getEquipe1Id());
            ps.setInt(6, matchGame.getEquipe2Id());
            ps.setInt(7, matchGame.getTournoiId());
            ps.setInt(8, matchGame.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM match_game WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<MatchGame> getAll() throws SQLException {
        String sql = "SELECT * FROM match_game";
        List<MatchGame> matchGames = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MatchGame matchGame = new MatchGame(
                        rs.getInt("id"),
                        rs.getTimestamp("date_match"),
                        (Integer) rs.getObject("score_team1"),
                        (Integer) rs.getObject("score_team2"),
                        rs.getString("statut"),
                        rs.getInt("equipe1_id"),
                        rs.getInt("equipe2_id"),
                        rs.getInt("tournoi_id")
                );
                matchGames.add(matchGame);
            }
        }

        return matchGames;
    }
}
