/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.address;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author srikanth
 * This Entity is used to save the address of donor
 */
@Entity
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable=true,name = "addressTypeId")
    private AddressType addressType;

    private String homeAddressline1;
    private String homeAddressline2;
    private String homeAddressCity;
    private String homeAddressProvince;
    private String homeAddressDistrict;
    private String homeAddressCountry;
    private String homeAddressState;
    private String homeAddressZipcode;
    
    private String workAddressline1;
    private String workAddressline2;
    private String workAddressCity;
    private String workAddressProvince;
    private String workAddressDistrict;
    private String workAddressCountry;
    private String workAddressState;
    private String workAddressZipcode;

    
    private String postalAddressline1;
    private String postalAddressline2;
    private String postalAddressCity;
    private String postalAddressProvince;
    private String postalAddressDistrict;
    private String postalAddressCountry;
    private String postalAddressState;
    private String postalAddressZipcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }
    

    public String getHomeAddressline1() {
        return homeAddressline1;
    }

    public void setHomeAddressline1(String homeAddressline1) {
        this.homeAddressline1 = homeAddressline1;
    }

    public String getHomeAddressline2() {
        return homeAddressline2;
    }

    public void setHomeAddressline2(String homeAddressline2) {
        this.homeAddressline2 = homeAddressline2;
    }

    public String getHomeAddressCity() {
        return homeAddressCity;
    }

    public void setHomeAddressCity(String homeAddressCity) {
        this.homeAddressCity = homeAddressCity;
    }

    public String getHomeAddressProvince() {
        return homeAddressProvince;
    }

    public void setHomeAddressProvince(String homeAddressProvince) {
        this.homeAddressProvince = homeAddressProvince;
    }

    public String getHomeAddressDistrict() {
        return homeAddressDistrict;
    }

    public void setHomeAddressDistrict(String homeAddressDistrict) {
        this.homeAddressDistrict = homeAddressDistrict;
    }

    public String getHomeAddressCountry() {
        return homeAddressCountry;
    }

    public void setHomeAddressCountry(String homeAddressCountry) {
        this.homeAddressCountry = homeAddressCountry;
    }

    public String getHomeAddressState() {
        return homeAddressState;
    }

    public void setHomeAddressState(String homeAddressState) {
        this.homeAddressState = homeAddressState;
    }

    public String getHomeAddressZipcode() {
        return homeAddressZipcode;
    }

    public void setHomeAddressZipcode(String homeAddressZipcode) {
        this.homeAddressZipcode = homeAddressZipcode;
    }

    public String getWorkAddressline1() {
        return workAddressline1;
    }

    public void setWorkAddressline1(String workAddressline1) {
        this.workAddressline1 = workAddressline1;
    }

    public String getWorkAddressline2() {
        return workAddressline2;
    }

    public void setWorkAddressline2(String workAddressline2) {
        this.workAddressline2 = workAddressline2;
    }

    public String getWorkAddressCity() {
        return workAddressCity;
    }

    public void setWorkAddressCity(String workAddressCity) {
        this.workAddressCity = workAddressCity;
    }

    public String getWorkAddressProvince() {
        return workAddressProvince;
    }

    public void setWorkAddressProvince(String workAddressProvince) {
        this.workAddressProvince = workAddressProvince;
    }

    public String getWorkAddressDistrict() {
        return workAddressDistrict;
    }

    public void setWorkAddressDistrict(String workAddressDistrict) {
        this.workAddressDistrict = workAddressDistrict;
    }

    public String getWorkAddressCountry() {
        return workAddressCountry;
    }

    public void setWorkAddressCountry(String workAddressCountry) {
        this.workAddressCountry = workAddressCountry;
    }

    public String getWorkAddressState() {
        return workAddressState;
    }

    public void setWorkAddressState(String workAddressState) {
        this.workAddressState = workAddressState;
    }

    public String getWorkAddressZipcode() {
        return workAddressZipcode;
    }

    public void setWorkAddressZipcode(String workAddressZipcode) {
        this.workAddressZipcode = workAddressZipcode;
    }

    public String getPostalAddressline1() {
        return postalAddressline1;
    }

    public void setPostalAddressline1(String postalAddressline1) {
        this.postalAddressline1 = postalAddressline1;
    }

    public String getPostalAddressline2() {
        return postalAddressline2;
    }

    public void setPostalAddressline2(String postalAddressline2) {
        this.postalAddressline2 = postalAddressline2;
    }

    public String getPostalAddressCity() {
        return postalAddressCity;
    }

    public void setPostalAddressCity(String postalAddressCity) {
        this.postalAddressCity = postalAddressCity;
    }

    public String getPostalAddressProvince() {
        return postalAddressProvince;
    }

    public void setPostalAddressProvince(String postalAddressProvince) {
        this.postalAddressProvince = postalAddressProvince;
    }

    public String getPostalAddressDistrict() {
        return postalAddressDistrict;
    }

    public void setPostalAddressDistrict(String postalAddressDistrict) {
        this.postalAddressDistrict = postalAddressDistrict;
    }

    public String getPostalAddressCountry() {
        return postalAddressCountry;
    }

    public void setPostalAddressCountry(String postalAddressCountry) {
        this.postalAddressCountry = postalAddressCountry;
    }

    public String getPostalAddressState() {
        return postalAddressState;
    }

    public void setPostalAddressState(String postalAddressState) {
        this.postalAddressState = postalAddressState;
    }

    public String getPostalAddressZipcode() {
        return postalAddressZipcode;
    }

    public void setPostalAddressZipcode(String postalAddressZipcode) {
        this.postalAddressZipcode = postalAddressZipcode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.address.Address[ id=" + id + " ]";
    }

}
