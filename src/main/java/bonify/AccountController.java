package bonify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

@RestController
public class AccountController {

    private int MAX_SPENDING_ALERT = 500;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private Environment env;

    @PutMapping("/transaction")
    public ResponseEntity<String> update(@RequestBody Transaction transaction) {
        boolean success = accountDAO.insert(transaction);

        if (!success) {
            return new ResponseEntity<>("Could not add transacton to the user account.", HttpStatus.BAD_REQUEST);
        }

        BigDecimal spending = accountDAO.getSpendingIn10DayWindow(transaction.getPartnerAccount(), transaction.getBookingDate());

        String line = transaction.getPartnerAccount() +
            ";" + OffsetDateTime.ofInstant(transaction.getBookingDate().toInstant(), ZoneId.systemDefault()) +
            ";" + spending + "\n";

        // If "test" environment...
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            writeToFile(line);
        }

        // If the user's spending in the last 10 days
        // is more than the defined threshold,
        // send the user a notification.
        if (spending.compareTo(BigDecimal.valueOf(MAX_SPENDING_ALERT)) > 0) {
            System.out.println("sending notification...");
        }

        return new ResponseEntity<>("Successful!", HttpStatus.OK);
    }

    /**
     * This is only used for testing.
     * <p>
     * Append the line to the file. Create the file if it doesn't exist yet.
     */
    private void writeToFile(String line) {
        Path file = Paths.get("test.actual.csv");
        try {
            Files.write(file, line.getBytes(), APPEND, CREATE);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
