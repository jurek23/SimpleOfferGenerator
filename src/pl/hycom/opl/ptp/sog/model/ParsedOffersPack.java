package pl.hycom.opl.ptp.sog.model;

import java.util.List;

/**
 * Zestaw wszystkich ofert, które udało się sparsować z CSV wraz z podziałem per formularz.
 * 
 * @author kgorecki
 *
 */
public class ParsedOffersPack {

    private List<SimpleOffer> allOffers;

    private List<SimpleOffer> neoInsOffers;
    private List<SimpleOffer> neoModOffers;
    private List<SimpleOffer> neoInsBizOffers;
    private List<SimpleOffer> neoModBizOffers;
    private List<SimpleOffer> trplInsOffers;
    private List<SimpleOffer> trplModOffers;

    public List<SimpleOffer> getAllOffers() {
        return allOffers;
    }

    public void setAllOffers(List<SimpleOffer> allOffers) {
        this.allOffers = allOffers;
    }

    public List<SimpleOffer> getNeoInsOffers() {
        return neoInsOffers;
    }

    public void setNeoInsOffers(List<SimpleOffer> neoInsOffers) {
        this.neoInsOffers = neoInsOffers;
    }

    public List<SimpleOffer> getNeoModOffers() {
        return neoModOffers;
    }

    public void setNeoModOffers(List<SimpleOffer> neoModOffers) {
        this.neoModOffers = neoModOffers;
    }

    public List<SimpleOffer> getNeoInsBizOffers() {
        return neoInsBizOffers;
    }

    public void setNeoInsBizOffers(List<SimpleOffer> neoInsBizOffers) {
        this.neoInsBizOffers = neoInsBizOffers;
    }

    public List<SimpleOffer> getNeoModBizOffers() {
        return neoModBizOffers;
    }

    public void setNeoModBizOffers(List<SimpleOffer> neoModBizOffers) {
        this.neoModBizOffers = neoModBizOffers;
    }

    public List<SimpleOffer> getTrplInsOffers() {
        return trplInsOffers;
    }

    public void setTrplInsOffers(List<SimpleOffer> trplInsOffers) {
        this.trplInsOffers = trplInsOffers;
    }

    public List<SimpleOffer> getTrplModOffers() {
        return trplModOffers;
    }

    public void setTrplModOffers(List<SimpleOffer> trplModOffers) {
        this.trplModOffers = trplModOffers;
    }

    @Override
    public String toString() {
        return "ParsedOffersPack [" + (allOffers != null ? "allOffers=" + allOffers.size() + ", " : "")
                + (neoInsOffers != null ? "neoInsOffers=" + neoInsOffers.size() + ", " : "")
                + (neoModOffers != null ? "neoModOffers=" + neoModOffers.size() + ", " : "")
                + (neoInsBizOffers != null ? "neoInsBizOffers=" + neoInsBizOffers.size() + ", " : "")
                + (neoModBizOffers != null ? "neoModBizOffers=" + neoModBizOffers.size() + ", " : "")
                + (trplInsOffers != null ? "trplInsOffers=" + trplInsOffers.size() + ", " : "")
                + (trplModOffers != null ? "trplModOffers=" + trplModOffers.size() : "") + "]";
    }

}
