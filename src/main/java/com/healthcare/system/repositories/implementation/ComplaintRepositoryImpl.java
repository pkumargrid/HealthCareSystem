package com.healthcare.system.repositories.implementation;

import com.healthcare.system.entities.Complaint;
import com.healthcare.system.exceptions.WrongCredentials;
import com.healthcare.system.repositories.ComplaintRepository;
import com.healthcare.system.repositories.PatientRepository;

import javax.sql.DataSource;
import java.rmi.ServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ComplaintRepositoryImpl implements ComplaintRepository {

    private final DataSource dataSource;
    private final PatientRepository patientRepository;

    public ComplaintRepositoryImpl(DataSource dataSource, PatientRepository patientRepository) {
        this.dataSource = dataSource;
        this.patientRepository = patientRepository;
    }

    @Override
    public Complaint findById(int id) throws WrongCredentials, ServerException {
        try (Connection connection = dataSource.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM complaint WHERE id = ?")) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return createComplaint(resultSet);
                    } else {
                        throw new WrongCredentials("Complaint with ID: " + id + " does not exist");
                    }
                }
            } catch (SQLException e) {
                throw new ServerException("Error accessing data: " + e.getMessage());
            }
        } catch (SQLException sqlException) {
            if ("08001".equals(sqlException.getSQLState())) {
                throw new ServerException("Could not connect to the server.");
            } else {
                throw new ServerException("Error executing SQL query: " + sqlException.getMessage());
            }
        }
    }

    @Override
    public void deleteById(int id) throws WrongCredentials, ServerException {
        try (Connection connection = dataSource.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement("delete from complaint where id = ?")){
                preparedStatement.setInt(1,id);
                try{
                    preparedStatement.executeUpdate();
                } catch (SQLException sqlException) {
                    throw new WrongCredentials(sqlException.getMessage());
                }
            } catch (SQLException e) {
                throw new ServerException("Error accessing data: " + e.getMessage());
            }
        } catch (SQLException sqlException) {
            if ("08001".equals(sqlException.getSQLState())) {
                throw new ServerException("Could not connect to the postgres server.");
            } else {
                throw new ServerException("Error executing SQL query: " + sqlException.getMessage());
            }
        }
    }

    @Override
    public void update(Complaint complaint) throws WrongCredentials, ServerException {
        try(Connection connection = dataSource.getConnection()) {
            String sql = "update complaint set text=?, type=?, table_name=?, patient_id=?,where id = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                configureComplaint(complaint, preparedStatement);
            } catch (SQLException e) {
                throw new ServerException("Error accessing data: " + e.getMessage());
            }
        } catch (SQLException sqlException) {
            if ("08001".equals(sqlException.getSQLState())) {
                throw new ServerException("Could not connect to the server.");
            } else {
                throw new ServerException("Error executing SQL query: " + sqlException.getMessage());
            }
        }
    }

    @Override
    public List<Complaint> findAll() throws ServerException, WrongCredentials {
        List<Complaint> complaints = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM complaint")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Complaint complaint = createComplaint(resultSet);
                    complaints.add(complaint);
                }
            } catch (SQLException e) {
                throw new ServerException("Error accessing data: " + e.getMessage());
            }
        } catch (SQLException sqlException) {
            if ("08001".equals(sqlException.getSQLState())) {
                throw new ServerException("Could not connect to the postgres server.");
            } else {
                throw new ServerException("Error executing SQL query: " + sqlException.getMessage());
            }
        }
        return complaints;
    }

    @Override
    public void save(Complaint complaint) throws WrongCredentials, ServerException {
        try {
            findById(complaint.getId());
        } catch (WrongCredentials wrongCredentials ) {
            try(Connection connection = dataSource.getConnection()) {
                String sql = "insert into complaint (text,type,table_name,patient_id, id,) values(?, ?, ?,?,?)";
                try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    configureComplaint(complaint, preparedStatement);
                } catch (SQLException e) {
                    throw new ServerException("Failed to execute the query: " + e.getMessage());
                }
            } catch (SQLException sqlException) {
                if ("08001".equals(sqlException.getSQLState())) {
                    throw new ServerException("Could not connect to the postgres server.");
                } else {
                    throw new ServerException("Error executing SQL query: " + sqlException.getMessage());
                }
            }
            return;
        }
        update(complaint);
    }

    @Override
    public List<Complaint> findComplainant(int id, String tableName) throws ServerException, WrongCredentials {
        List<Complaint> complaints = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM complaint where table_name=? and type = ? ")) {
            preparedStatement.setString(1, tableName);
            preparedStatement.setInt(2, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Complaint complaint = createComplaint(resultSet);
                    complaints.add(complaint);
                }
            } catch (SQLException e) {
                throw new ServerException("Error accessing data: " + e.getMessage());
            }
        } catch (SQLException sqlException) {
            if ("08001".equals(sqlException.getSQLState())) {
                throw new ServerException("Could not connect to the postgres server.");
            } else {
                throw new ServerException("Error executing SQL query: " + sqlException.getMessage());
            }
        }
        return complaints;
    }

    private Complaint createComplaint(ResultSet resultSet) throws SQLException, ServerException, WrongCredentials {
        Complaint complaint = new Complaint();
        complaint.setText(resultSet.getString("text"));
        complaint.setId(resultSet.getInt("id"));
        complaint.setTableName(resultSet.getString("table_name"));
        complaint.setType(resultSet.getInt("type"));
        complaint.setPatient(patientRepository.findById(resultSet.getInt("patient")));
        return complaint;
    }

    private void configureComplaint(Complaint complaint, PreparedStatement preparedStatement) throws WrongCredentials, SQLException {
        preparedStatement.setString(1, complaint.getText());
        preparedStatement.setInt(2,complaint.getType());
        preparedStatement.setString(3,complaint.getTableName());
        preparedStatement.setInt(4, complaint.getPatient().getId());
        preparedStatement.setInt(5, complaint.getId());
        try{
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new WrongCredentials(e.getMessage());
        }
    }
}
