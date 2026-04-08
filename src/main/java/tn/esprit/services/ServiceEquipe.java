package org.example.services;

import org.example.entities.Equipe;
import org.example.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceEquipe implements IService<Equipe> {
    private Connection conn;

    public ServiceEquipe() {
        conn = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Equipe equipe) throws SQLException {
        String sql = "INSERT INTO equipe(nom, max_members, logo, owner_id) VALUES (?, ?, ?, 10)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipe.getNom());
            ps.setInt(2, equipe.getMaxMembers());
            ps.setString(3, equipe.getLogo());
            ps.executeUpdate();
        }
    }

    @Override
    public void modifier(Equipe equipe) throws SQLException {
        String sql = "UPDATE equipe SET nom=?, max_members=?, logo=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipe.getNom());
            ps.setInt(2, equipe.getMaxMembers());
            ps.setString(3, equipe.getLogo());
            ps.setInt(4, equipe.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM equipe WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Equipe> getAll() throws SQLException {
        String sql = "SELECT * FROM equipe";
        List<Equipe> equipes = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Equipe equipe = new Equipe(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("max_members"),
                    rs.getString("logo")
                );
                equipes.add(equipe);
            }
        }

        return equipes;
    }
}
