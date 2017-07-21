package bonify;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

@RestController()
public class AccountController {

    private int MAX_SPENDING_ALERT = 500;

    @PutMapping("/transaction")
    public void update(@RequestBody Transaction transaction) {
        AccountDAO.insert(transaction);

        try {
            BigDecimal spending = AccountDAO.getSpendingIn10DayWindow(transaction.getPartnerAccount(), transaction.getBookingDate());

            String line = transaction.getPartnerAccount() +
                ";" + OffsetDateTime.ofInstant(transaction.getBookingDate().toInstant(), ZoneId.systemDefault()) +
                ";" + spending + "\n";

            writeToFile(line);

            // If the user's spending in the last 10 days
            // is more than the defined threshold,
            // send the user a notification.
            if (spending.compareTo(BigDecimal.valueOf(MAX_SPENDING_ALERT)) > 0) {
                System.out.println("sending notification...");
            }
        } catch (java.sql.SQLException e) {
            // do nothing
        }
    }

    private void writeToFile(String line) {
        Path file = Paths.get("test.actual.csv");
        try {
            Files.write(file, line.getBytes(), APPEND, CREATE);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
