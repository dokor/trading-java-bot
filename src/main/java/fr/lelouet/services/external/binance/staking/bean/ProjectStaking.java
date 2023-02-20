package fr.lelouet.services.external.binance.staking.bean;

public record ProjectStaking(
    String projectId,
    Detail detail,
    Quota quota

) {

/**
 * [
 *   {
 *     "projectId": "Axs*90",
 *     "detail": {
 *       "asset": "AXS",
 *       "rewardAsset": "AXS",
 *       "duration": 90,
 *       "renewable": true,
 *       "apy": "1.2069"
 *     },
 *     "quota": {
 *       "totalPersonalQuota": "2",
 *       "minimum": "0.001"
 *     }
 *   }
 * ]
 */

}
