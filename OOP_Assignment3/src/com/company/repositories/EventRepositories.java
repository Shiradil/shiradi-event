package com.company.repositories;

import com.company.data.interfaces.IDB;
import com.company.entities.Event;
import com.company.entities.User;
import com.company.repositories.interfaces.IEventRepositories;


import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class EventRepositories implements IEventRepositories {
    private final IDB db;
    private Connection con = null;
    public EventRepositories(IDB db){this.db = db;}

    @Override
    public boolean CreateEvent(Event event) {
        this.con = null;
        try {
            this.con = db.getConnection();
            String sql = "INSERT INTO events(name,price,description) VALUES (?,?,?)";
            PreparedStatement st = this.con.prepareStatement(sql);

            st.setString(1, event.getName());
            st.setDouble(2, event.getPrice());
            st.setString(3, event.getDescription());

            st.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                this.con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean DeleteEvent(Event event) {
        try {
            this.con = db.getConnection();
            String sql = "DELETE FROM events WHERE id = ?";
            PreparedStatement st = this.con.prepareStatement(sql);

            st.setInt(1, event.getId());
            st.executeUpdate();

            st.close();
            this.con.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting event: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public List<Event> getAllEvents() {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT id,name,price,description FROM events";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);
            List<Event> events = new LinkedList<>();
            while (rs.next()) {
                Event event = new Event(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"));

                events.add(event);
            }

            return events;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }
}
