package fr.lelouet.services.external.binance.config.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(staticName = "of")
@Getter
@Setter
public class BinanceResponse<T> {
    private T answer;
}
