package brtApp.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CdrDto {
    @Pattern(regexp = "01|02", message = "Flag must be either '01' or '02'")
    String flag;
    @Pattern(regexp = "^7\\d{10}$", message = "Initiator must be 11 digits starting with 7")
    String initiator;
    @Pattern(regexp = "^7\\d{10}$", message = "Receiver must be 11 digits starting with 7")
    String receiver;
    @NotNull
    LocalDateTime startDate;
    @NotNull
    LocalDateTime endDate;

    @AssertTrue(message = "Start date must be before end date")
    private boolean isStartDateBeforeEndDate() {
        return startDate.isBefore(endDate);
    }

    @AssertTrue(message = "Initiator equals receiver")
    private boolean isInitiatorEqualsReceiver(){return !initiator.equals(receiver);}

    public String getOwner() {

        if (this.getFlag().equals("01")) {
            return this.getInitiator();
        } else {
            return this.getReceiver();
        }
    }
}

