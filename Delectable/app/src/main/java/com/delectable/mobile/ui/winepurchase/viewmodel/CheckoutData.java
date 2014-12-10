package com.delectable.mobile.ui.winepurchase.viewmodel;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.PaymentMethod;
import com.delectable.mobile.api.models.Pricing;
import com.delectable.mobile.api.models.PurchaseOffer;
import com.delectable.mobile.api.models.ShippingAddress;
import com.delectable.mobile.api.models.WineProfileMinimal;

public class CheckoutData {

    private PurchaseOffer mPurchaseOffer;

    private WineProfileMinimal mWineProfileMinimal;

    private Pricing mCurrentPrice;

    private ShippingAddress mSelectedShippingAddress;

    private PaymentMethod mSelectedPaymentMethod;

    private int mQuantity = -1;

    private int mMinNumBottles = 1;

    private int mMaxNumBottles = 1;

    public CheckoutData() {
    }

    //region Getters/Setters
    public boolean isWineProfileLoaded() {
        return mWineProfileMinimal != null;
    }

    public String getProducerName() {
        if (mWineProfileMinimal == null) {
            return "";
        }
        return mWineProfileMinimal.getProducerName();
    }

    public String getWineNameWithVintage() {
        if (mWineProfileMinimal == null) {
            return "";
        }
        return mWineProfileMinimal.getName() + " " + mWineProfileMinimal.getVintage();
    }

    public String getVintage() {
        return mWineProfileMinimal.getVintage();
    }

    public ShippingAddress getSelectedShippingAddress() {
        return mSelectedShippingAddress;
    }

    public void setSelectedShippingAddress(ShippingAddress selectedShippingAddress) {
        mSelectedShippingAddress = selectedShippingAddress;
    }

    public String getAddressDisplayText() {
        if (mSelectedShippingAddress == null) {
            return "";
        }
        String address = mSelectedShippingAddress.getAddr1();
        address += mSelectedShippingAddress.getAddr2() == null ? ""
                : " " + mSelectedShippingAddress.getAddr2();
        return address.trim() +
                ", " + mSelectedShippingAddress.getCity()
                + ", " + mSelectedShippingAddress.getState();
    }

    public String getShippingState() {
        if (mSelectedShippingAddress == null) {
            return null;
        }
        return mSelectedShippingAddress.getState();
    }

    public PaymentMethod getSelectedPaymentMethod() {
        return mSelectedPaymentMethod;
    }

    public void setSelectedPaymentMethod(PaymentMethod selectedPaymentMethod) {
        mSelectedPaymentMethod = selectedPaymentMethod;
    }

    public String getPaymentMethodDisplayText() {
        if (mSelectedPaymentMethod == null) {
            return "";
        }
        return mSelectedPaymentMethod.getLastFour();
    }

    public int getPaymentMethodIconResId() {
        if (mSelectedPaymentMethod.isAmex()) {
            return R.drawable.ic_amex;
        } else if (mSelectedPaymentMethod.isMastercard()) {
            return R.drawable.ic_mastercard;
        } else if (mSelectedPaymentMethod.isDiscover()) {
            return R.drawable.ic_discover;
        } else if (mSelectedPaymentMethod.isVisa()) {
            return R.drawable.ic_visa;
        }
        return 0;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public String getPerBottleText() {
        return mWineProfileMinimal.getPriceText();
    }

    public String getTotalPriceText() {
        if (mCurrentPrice == null) {
            return "-";
        }
        return mCurrentPrice.getTotal();
    }

    public String getShippingPriceText() {
        if (mCurrentPrice == null) {
            return "-";
        }
        return mCurrentPrice.getShipping();
    }

    public String getTaxPriceText() {
        if (mCurrentPrice == null) {
            return "-";
        }
        return mCurrentPrice.getTax();
    }
    //endregion

    public String getPromoPriceText() {
        // TODO: Get Promo Amount Text once Promo Code is Implemented
        return null;
    }

    //region modifiers
    public void updateWithData(PurchaseOffer purchaseOffer,
            WineProfileMinimal wineProfileMinimal) {
        mPurchaseOffer = purchaseOffer;
        mWineProfileMinimal = wineProfileMinimal;

        mMinNumBottles = mPurchaseOffer.getMinQuant();
        mMaxNumBottles = mPurchaseOffer.getMaxQuant();
        // Don't reset Quantity if data changed:
        if (mQuantity == -1) {
            mQuantity = mPurchaseOffer.getDefaultQuant();
        }
        updatePricingData();
    }

    public void addBottle() {
        if (mQuantity == mMaxNumBottles || mPurchaseOffer == null) {
            return;
        }
        mQuantity++;
        updatePricingData();
    }

    public void removeBottle() {
        if (mQuantity == mMinNumBottles || mPurchaseOffer == null) {
            return;
        }
        mQuantity--;
        updatePricingData();
    }
    //endregion

    //region helpers
    private void updatePricingData() {
        if (mPurchaseOffer == null || mPurchaseOffer.getPricing() == null) {
            return;
        }

        for (Pricing pricing : mPurchaseOffer.getPricing()) {
            if (pricing.getQuantity() == mQuantity) {
                mCurrentPrice = pricing;
                break;
            }
        }
    }

    public boolean isDataValid() {
        if (mSelectedPaymentMethod == null) {
            return false;
        }
        if (mSelectedShippingAddress == null) {
            return false;
        }
        if (mCurrentPrice == null) {
            return false;
        }
        if (mWineProfileMinimal == null) {
            return false;
        }
        return true;
    }

    public String getMarketingMessage() {
        if (mPurchaseOffer == null) {
            return null;
        }
        return mPurchaseOffer.getMarketingMessage();
    }

    public String getWineId() {
        return mWineProfileMinimal.getId();
    }

    public String getPurchaseOfferId() {
        return mPurchaseOffer.getId();
    }

    public String getPaymentMethodId() {
        return mSelectedPaymentMethod.getId();
    }

    public String getShippingAddressId() {
        return mSelectedShippingAddress.getId();
    }

    public double getTotalPriceValue() {
        double price = 0.0;
        String priceText = getTotalPriceText().replace("$", "");
        try {
            price = Double.valueOf(priceText);
        } catch (NumberFormatException ex) {
            // no-op, issue with price?
        }
        return price;
    }
    //endregion
}
