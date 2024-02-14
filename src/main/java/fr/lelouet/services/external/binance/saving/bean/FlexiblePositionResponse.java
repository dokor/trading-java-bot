package fr.lelouet.services.external.binance.saving.bean;

import java.util.List;

public record FlexiblePositionResponse(
    Integer total,
    List<FlexiblePositionByAsset> rows
) {
}
