

package model.donationbatch;

/**
 *
 * @author srikanth
 */
public class DonationBatchSessionSingleton {
    
    private static volatile  DonationBatchSessionSingleton instance;
    private static DonationBatch donationBatch = null;
    
   public static DonationBatchSessionSingleton getInstance(){
       if(instance == null) {
         instance = new DonationBatchSessionSingleton();
      }
      return instance;
   }
   
   public static void clear(){
       instance = new DonationBatchSessionSingleton();
       instance.donationBatch = null;
   }
   
   public void setDonationBatch(DonationBatch donationBatch){
       instance.donationBatch = donationBatch;
   }
   
   public DonationBatch getDonationBatch( ){
       return instance.donationBatch;
   }
    
}
