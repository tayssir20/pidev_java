package tn.esprit.services;

import tn.esprit.entities.Personne;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePersonne implements IService<Personne>{
    private Connection conn;

    public ServicePersonne() {
        conn= MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Personne personne) throws SQLException {
       String requete= "insert into personne(nom,prenom,age) values('"+personne.getNom()+"','"+personne.getPrenom()+"','"+personne.getAge()+"')";
       Statement stmt= conn.createStatement();
       stmt.executeUpdate(requete);
    }

    @Override
    public void modifier(Personne personne) throws SQLException {
        String requete="update personne set nom=?,prenom=?,age=? where id=?";
        PreparedStatement preparedStatement= conn.prepareStatement(requete);
        preparedStatement.setString(1,personne.getNom());
        preparedStatement.setString(2,personne.getPrenom());
        preparedStatement.setInt(3,personne.getAge());
        preparedStatement.setInt(4,personne.getId());
        preparedStatement.executeUpdate();

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM personne WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ps.executeUpdate();

    }

    @Override
    public List<Personne> getAll() throws SQLException {
        String requete="select * from personne";
        Statement stmt= conn.createStatement();
        ResultSet rs= stmt.executeQuery(requete);
        List<Personne> listpersonnes= new ArrayList<Personne>();
        while(rs.next())
        {
            Personne p= new Personne(
                    rs.getString(2),
                    rs.getString("prenom"),
                    rs.getInt("age"));
            listpersonnes.add(p);
        }
        return listpersonnes;
    }
}
