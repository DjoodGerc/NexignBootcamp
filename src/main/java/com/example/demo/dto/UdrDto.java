package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UdrDto {
    private String msisdn;
    private IncomingCall incomingCall;
    private OutcomingCall outcomingCall;

    public UdrDto(String number, Duration incomingDuration, Duration outcomingDuration) {
        this.msisdn=number;
        IncomingCall incomingCall=new IncomingCall();
        incomingCall.totalTime= DurationFormatUtils.formatDuration(incomingDuration.toMillis(), "HH:mm:ss");
        this.incomingCall=incomingCall;

        OutcomingCall outcomingCall=new OutcomingCall();
        outcomingCall.totalTime= DurationFormatUtils.formatDuration(outcomingDuration.toMillis(), "HH:mm:ss");
        this.outcomingCall=outcomingCall;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class IncomingCall{
        String totalTime;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private  class OutcomingCall{
        String totalTime;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }


}
