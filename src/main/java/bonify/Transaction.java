package bonify;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {

    private long id;
    @JsonProperty(value = "Partner_Account")
    private String partnerAccount;
    @JsonProperty(value = "Partner_BLZ")
    private String partnerBLZ;
    @JsonProperty(value = "Bank_Name")
    private String bankName;
    @JsonProperty(value = "Partner_Name")
    private String partnerName;
    @JsonProperty(value = "Booking_Text")
    private String bookingText;
    @JsonProperty(value = "Subject")
    private String subject;
    @JsonProperty(value = "Booking_Date")
    @JsonFormat(locale = "US", pattern = "EEE MMM dd hh:mm:ss Z yyyy")
    private Date bookingDate;
    @JsonProperty(value = "Transfer_Type")
    private String transferType;
    @JsonProperty(value = "Currency")
    private String currency;
    @JsonProperty(value = "Amount")
    private BigDecimal amount;

    public Transaction() {
    }

    public Transaction(
        Long id,
        String partnerAccount,
        String partnerBLZ,
        String bankName,
        String partnerName,
        String bookingText,
        String subject,
        Date bookingDate,
        String transferType,
        String currency,
        BigDecimal amount
    ) {
        this.id = id;
        this.partnerAccount = partnerAccount;
        this.partnerBLZ = partnerBLZ;
        this.bankName = bankName;
        this.partnerName = partnerName;
        this.bookingText = bookingText;
        this.subject = subject;
        this.bookingDate = bookingDate;
        this.transferType = transferType;
        this.currency = currency;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getPartnerAccount() {
        return partnerAccount;
    }

    public String getPartnerBLZ() {
        return partnerBLZ;
    }

    public String getBankName() {
        return bankName;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getBookingText() {
        return bookingText;
    }

    public String getSubject() {
        return subject;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public String getTransferType() {
        return transferType;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
