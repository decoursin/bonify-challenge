package bonify;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Repository
public class AccountDAO {

    /**
     * Returns the user's spending in the past 10 days.
     *
     * Notice, the user's income (positive transactions) is not added to spending, but filtered out.
     *
     * @param partnerAccount User's account number
     * @param bookingDate the Date of purchase
     *
     * @return the amount of spending: a positive number.
     */
    public static BigDecimal getSpendingIn10DayWindow(String partnerAccount, Date bookingDate) throws java.sql.SQLException {
        try {
            System.out.println("partnerAccount=" + partnerAccount + " bookingDate=" + bookingDate);

            Date start = Date.from(bookingDate.toInstant().minus(Duration.ofDays(10)));

            JdbcSession session = new JdbcSession(DatabaseConfig.DATA_SOURCE.getConnection())
                .autocommit(false)
                .sql("select sum(amount) " +
                    " from account" +
                    " where partner_account = ?" +
                    " and amount < 0" +
                    " and booking_date > CAST (? as timestamp with time zone)" +
                    " and booking_date <= CAST (? as timestamp with time zone)")
                .set(partnerAccount)
                .set(start)
                .set(bookingDate);

            BigDecimal spending = Optional.ofNullable(session.select(new SingleOutcome<>(String.class)))
                .map(BigDecimal::new)
                .orElse(BigDecimal.ZERO)
                .abs();

            System.out.println("spending=" + spending);

            session.commit();

            return spending;
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }
    }

    public static void insert(Transaction transaction) {
        try {
            JdbcSession session = new JdbcSession(DatabaseConfig.DATA_SOURCE.getConnection())
                .autocommit(false)
                .sql("INSERT INTO account " +
                    " (partner_account, partner_blz, bank_name, partner_name, booking_text, subject, booking_date, transfer_type, currency, amount)" +
                    " VALUES (?,?,?,?,?,?, CAST (? AS timestamp with time zone) ,?,?, CAST (? AS numeric))")
                .set(transaction.getPartnerAccount())
                .set(transaction.getPartnerBLZ())
                .set(transaction.getBankName())
                .set(transaction.getPartnerName())
                .set(transaction.getBookingText())
                .set(transaction.getSubject())
                .set(transaction.getBookingDate())
                .set(transaction.getTransferType())
                .set(transaction.getCurrency())
                .set(transaction.getAmount());

            Integer update = session.insert(Outcome.UPDATE_COUNT);

            System.out.println("update: " + update);

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
