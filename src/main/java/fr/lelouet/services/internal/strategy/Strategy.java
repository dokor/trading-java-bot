package fr.lelouet.services.internal.strategy;

public interface Strategy {
    /**
     * Actions à réaliser lorsque les cryptos du wallet de l'utilisateur se mettent à jour
     */
    void onAccountUpdated();

    /**
     * Actions à réaliser lorsque les données techniques se mettent à jour
     */
    void onTickersUpdated();

    /**
     * Actions à réaliser lorsque des ordres d'achats ou de ventes sont lancer ou mis à jour
     */
    void onOrdersUpdated();

    /**
     * Actions à réaliser lorsque des trades sont réalisés
     * Un trade est un order qui a été éxécuté dans l'exchange.
     */
    void onTradesUpdated();

    /**
     * Actions à réaliser lorsque les positions sont mis à jours
     * Une position est l'ensemble des trades d'une meme pair dans le meme sens
     * ex : position d'achat BTC/USDT => L'ensemble des trades de la pair BTC/USDT qui cherchent à acheter du BTC
     */
    void onPositionsUpdated();

    /**
     * Actions à réaliser lorsque le statuts d'une positions est mis a jour
     */
    void onPositionsStatusUpdates();
}
