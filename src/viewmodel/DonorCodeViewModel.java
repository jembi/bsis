/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package viewmodel;

import model.donorcodes.DonorDonorCode;

/**
 *
 * @author srikanth
 */
public class DonorCodeViewModel {
    
    private DonorDonorCode donorDonorCode;
    public DonorCodeViewModel(){
        
    }
    
    public DonorCodeViewModel(DonorDonorCode donorDonorCode){
        this.donorDonorCode = donorDonorCode;
    }
    
    public Long getDonorCodeId(){
        return donorDonorCode.getDonorCode().getId();
    }
    
    public String getDonorCodeName(){
        return  donorDonorCode.getDonorCode().getDonorCode();
    }
    
    public String getDonorCodeGroupName(){
        return donorDonorCode.getDonorCode().getDonorCodeGroup().getDonorCodeGroup();
    }
}
