package bonify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Repository
public class AccountDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Returns the user's spending in the past 10 days.
     *
     * Notice, the user's income (positive transactions) is not added to spending, but filtered out.
     *
     * @param partnerAccount User's account number
     * @param bookingDate    the Date of purchase
     * @return the amount of spending: a positive number.
     */
    public BigDecimal getSpendingIn10DayWindow(String partnerAccount, Date bookingDate) {
        System.out.println("partnerAccount=" + partnerAccount + " bookingDate=" + bookingDate);

        Date start = Date.from(bookingDate.toInstant().minus(Duration.ofDays(10)));

        String sql = " select sum(amount) " +
            " from account" +
            " where partner_account = ?" +
            " and amount < 0" +
            " and booking_date > CAST (? as timestamp with time zone)" +
            " and booking_date <= CAST (? as timestamp with time zone)";

        BigDecimal spending = Optional.ofNullable(jdbcTemplate.queryForObject(
            sql,
            String.class,
            partnerAccount,
            start,
            bookingDate
        ))
            .map(BigDecimal::new)
            .orElse(BigDecimal.ZERO)
            .abs();

        System.out.println("spending=" + spending);

        return spending;
    }

    @Transactional
    public Boolean insert(Transaction transaction) {
        String sql = "INSERT INTO account " +
            " (partner_account, partner_blz, bank_name, partner_name, booking_text, subject, booking_date, transfer_type, currency, amount)" +
            " VALUES (?,?,?,?,?,?, CAST (? AS timestamp with time zone) ,?,?, CAST (? AS numeric))";

        int success = jdbcTemplate.update(
            sql,
            transaction.getPartnerAccount(),
            transaction.getPartnerBLZ(),
            transaction.getBankName(),
            transaction.getPartnerName(),
            transaction.getBookingText(),
            transaction.getSubject(),
            transaction.getBookingDate(),
            transaction.getTransferType(),
            transaction.getCurrency(),
            transaction.getAmount()
        );

        return success > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
}
