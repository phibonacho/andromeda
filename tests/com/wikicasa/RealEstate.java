package com.wikicasa;

import com.annotation.validate.Validate;
import com.annotation.validate.types.*;

public class RealEstate {
    private String externalId;
    private String description;
    private boolean sale;
    private boolean rent;
    private boolean bareOwnership;
    private boolean auction;
    private double priceRent;
    private double priceSale;
    private double priceBareOwnership;
    private boolean reservedPrice;
    private long macroTypology;
    private boolean exclusive;
    private double sqm;
    private double ipe;
    private Address address;

    @Validate(with = StringValue.class, mandatory = true)
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Validate(with = StringValue.class, mandatory = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Validate(with = BooleanValue.class, mandatory = true, require = "getPriceSale", alternatives = "isRent")
    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    @Validate(with = BooleanValue.class, require = "getPriceRent")
    public boolean isRent() {
        return rent;
    }

    public void setRent(boolean rent) {
        this.rent = rent;
    }

    @Validate(with = BooleanValue.class, require = {"getBareOwnershipPrice", "isSale"})
    public boolean isBareOwnership() {
        return bareOwnership;
    }

    public void setBareOwnership(boolean bareOwnership) {
        this.bareOwnership = bareOwnership;
    }

    public boolean isAuction() {
        return auction;
    }

    public void setAuction(boolean auction) {
        this.auction = auction;
    }

    @Validate(with = DPositive.class)
    public double getPriceRent() {
        return priceRent;
    }

    public void setPriceRent(double priceRent) {
        this.priceRent = priceRent;
    }

    @Validate(with = DPositive.class)
    public double getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(double priceSale) {
        this.priceSale = priceSale;
    }

    @Validate(with = DoubleValue.class)
    public double getPriceBareOwnership() {
        return priceBareOwnership;
    }

    public void setPriceBareOwnership(double priceBareOwnership) {
        this.priceBareOwnership = priceBareOwnership;
    }

    @Validate(with = BooleanValue.class)
    public boolean isReservedPrice() {
        return reservedPrice;
    }

    public void setReservedPrice(boolean reservedPrice) {
        this.reservedPrice = reservedPrice;
    }

    @Validate(with = LongValue.class, mandatory = true)
    public long getMacroTypology() {
        return macroTypology;
    }

    public void setMacroTypology(long macroTypology) {
        this.macroTypology = macroTypology;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    @Validate(with = DPositive.class, mandatory = true)
    public double getSqm() {
        return sqm;
    }

    public void setSqm(double sqm) {
        this.sqm = sqm;
    }

    @Validate(with = DoubleValue.class, mandatory = true)
    public double getIpe() {
        return ipe;
    }

    public void setIpe(double ipe) {
        this.ipe = ipe;
    }

    @Validate(with = NestedVal.class, mandatory = true)
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
