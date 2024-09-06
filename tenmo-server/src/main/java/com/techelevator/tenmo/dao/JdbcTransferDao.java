package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Transfer> getTransfersByUserID(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, " +
                "tt.transfer_type_desc AS transfer_type, " +
                "ts.transfer_status_desc AS transfer_status, " +
                "u_from.username AS account_from_username, " +
                "u_to.username AS account_to_username, " +
                "t.amount " +
                " FROM transfer t " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN account a_from ON t.account_from = a_from.account_id " +
                "JOIN account a_to ON t.account_to = a_to.account_id " +
                "JOIN tenmo_user u_from ON a_from.user_id = u_from.user_id " +
                "JOIN tenmo_user u_to ON a_to.user_id = u_to.user_id " +
                "WHERE a_from.user_id = ? " +
                "OR a_to.user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (results.next()) {
                Transfer transfer = mapRow(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error accessing database: " + e.getMessage(), e);
        }
        return transfers;
    }


    @Override
    public int createTransfer(TransferDto transferDto) {
        String sql = "INSERT INTO transfer " +
                "(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?,?,(SELECT account_id FROM account WHERE user_id = ?)," +
                "(SELECT account_id FROM account where user_id = ?),?) " +
                "RETURNING transfer_id";
        try {
            Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class,
                    transferDto.getTransferType(), transferDto.getTransferStatus(), transferDto.getAccountFromUsername(), transferDto.getAccountToUsername(), transferDto.getAmount());
            if (transferId != null) {
                return transferId;
            } else {
                throw new DaoException("Failed to retrieve the transfer id");
            }
        } catch (DataIntegrityViolationException e) {
            //Getting failure seems to be a foreign key issue
            System.out.println(e.getMessage());
            throw new DaoException("The transaction already exists", e);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error accessing database: " + e.getMessage(), e);
        }
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT t.transfer_id, " +
                "tt.transfer_type_desc AS transfer_type, " +
                "ts.transfer_status_desc AS transfer_status, " +
                "u_from.username AS account_from_username, " +
                "u_to.username AS account_to_username, " +
                "t.amount " +
                " FROM transfer t " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN account a_from ON t.account_from = a_from.account_id " +
                "JOIN account a_to ON t.account_to = a_to.account_id " +
                "JOIN tenmo_user u_from ON a_from.user_id = u_from.user_id " +
                "JOIN tenmo_user u_to ON a_to.user_id = u_to.user_id " +
                "WHERE t.transfer_id = ? ";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRow(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error accessing database: " + e.getMessage(), e);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getPendingTransfersByUserID(int userID) {

        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, " +
                "tt.transfer_type_desc AS transfer_type, " +
                "ts.transfer_status_desc AS transfer_status, " +
                "u_from.username AS account_from_username, " +
                "u_to.username AS account_to_username, " +
                "t.amount " +
                " FROM transfer t " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN account a_from ON t.account_from = a_from.account_id " +
                "JOIN account a_to ON t.account_to = a_to.account_id " +
                "JOIN tenmo_user u_from ON a_from.user_id = u_from.user_id " +
                "JOIN tenmo_user u_to ON a_to.user_id = u_to.user_id " +
                "WHERE a_from.user_id = ? " +
                "AND ts.transfer_status_desc = 'Pending'";


        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID);

            while (results.next()) {
                Transfer transfer = mapRow(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error accessing database: " + e.getMessage(), e);
        }
        return transfers;
    }


    private Transfer mapRow(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferType(rs.getString("transfer_type"));
        transfer.setTransferStatus(rs.getString("transfer_status"));
        transfer.setAccountFromUsername(rs.getString("account_from_username"));
        transfer.setAccountToUsername(rs.getString("account_to_username"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }

}
