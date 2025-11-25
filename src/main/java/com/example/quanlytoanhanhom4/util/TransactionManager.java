package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * Utility class for managing database transactions.
 */
public final class TransactionManager {

    private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);

    private TransactionManager() {
        // Utility class
    }

    /**
     * Executes a function within a database transaction.
     * Automatically commits on success or rolls back on failure.
     * 
     * @param operation The operation to execute
     * @param <T> The return type
     * @return The result of the operation
     * @throws Exception if the operation fails
     */
    public static <T> T executeInTransaction(Function<Connection, T> operation) throws Exception {
        Connection conn = null;
        boolean originalAutoCommit = true;
        
        try {
            conn = DatabaseConnection.getConnection();
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            
            T result = operation.apply(conn);
            
            conn.commit();
            logger.debug("Transaction committed successfully");
            return result;
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.warn("Transaction rolled back due to error", e);
                } catch (SQLException rollbackEx) {
                    logger.error("Lỗi khi rollback transaction", rollbackEx);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommit);
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Lỗi khi đóng connection", e);
                }
            }
        }
    }

    /**
     * Executes a void operation within a database transaction.
     * 
     * @param operation The operation to execute
     * @throws Exception if the operation fails
     */
    public static void executeInTransaction(TransactionOperation operation) throws Exception {
        try {
            executeInTransaction(conn -> {
                try {
                    operation.execute(conn);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            }
            throw e;
        }
    }

    /**
     * Functional interface for transaction operations that don't return a value.
     */
    @FunctionalInterface
    public interface TransactionOperation {
        void execute(Connection conn) throws Exception;
    }
}

