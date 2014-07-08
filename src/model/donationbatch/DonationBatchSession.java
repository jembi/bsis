/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.donationbatch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author srikanth
 */
@Entity
public class DonationBatchSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, columnDefinition = "SMALLINT")
    private Integer id;

    @OneToOne
    private DonationBatch donationBatch;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DonationBatch getDonationBatch() {
        return donationBatch;
    }

    public void setDonationBatch(DonationBatch donationBatch) {
        this.donationBatch = donationBatch;
    }
    
    
}
