package edu.ttu.spm.cheapride;

/**
 * the data structure of history record
 */

public class historyRecordEntity {
    private String date;
    private String provider;
    private String pickup;
    private String destination;
    private String fee;

    public historyRecordEntity(String date,String provider,String pick,String destination,String fee){
        this.date = date;
        this.provider = provider;
        this.pickup = pick;
        this.destination = destination;
        this.fee = fee;
    }
    public String getDate(){return date;}
    public String getProvider(){return provider;}
    public String getPick(){return pickup;}
    public String getDestination(){return destination;}
    public String getFee(){return fee;}
}
