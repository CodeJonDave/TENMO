package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferStatusDAO implements TransferStatusDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int getTransferStatusIdFromDesc(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Transfer status description cannot be null or empty");
        }
        String sql = "SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = ?";
        try {
            Integer id = jdbcTemplate.queryForObject(sql, Integer.class, description);
            if (id == null) {
                throw new DaoException("No transfer status found for description: " + description);
            }
            return id;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to the database", e);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("No transfer status found for description: " + description, e);
        } catch (DataAccessException e) {
            throw new DaoException("An error occurred while accessing the database", e);
        }
    }

    @Override
    public boolean updateTransferStatus(int transferId, int status) {
         String sql = "UPDATE transfer " +
                "SET transfer_status_id = ? " +
                "WHERE transfer_id = ?";


        System.out.println("transferId " + transferId);
        System.out.println("statusId; " + status);

        try {
            int rowsAffected = jdbcTemplate.update(sql, status, transferId);
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

}
