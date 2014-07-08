

package model.donationbatch;

/**
 *
 * @author srikanth
 */
public class BatchSessionSingleton {
    
    private static volatile  BatchSessionSingleton instance;
    private static DonationBatch donationBatch = null;
    
   public static BatchSessionSingleton getInstance(){
       if(instance == null) {
         instance = new BatchSessionSingleton();
      }
      return instance;
   }
   
   public static void clear(){
       instance = new BatchSessionSingleton();
       instance.donationBatch = null;
   }
   
   public void setDonationBatch(DonationBatch donationBatch){
       instance.donationBatch = donationBatch;
   }
   
   public DonationBatch getDonationBatch( ){
       return instance.donationBatch;
   }
    
}
