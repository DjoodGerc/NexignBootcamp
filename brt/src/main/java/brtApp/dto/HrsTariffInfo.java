package brtApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrsTariffInfo {
    private Long id;

    String name;
    @JsonProperty("active")
    boolean isActive;

    LocalDateTime creationDate;

    String description;

    TariffParameterEntity tariffParametr;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class TariffParameterEntity {
        private Long id;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public class TariffTypeEntity {

            private Long id;

            String name;
        }

        TariffTypeEntity tariffType;

        private Double initiatingInternalCallCost;

        private Double receivingInternalCallCost;

        private Double initiatingExternalCallCost;

        private Double receivingExternalCallCost;

        private Long monthlyMinuteCapacity;

        private Double monthlyFee;
    }
}

