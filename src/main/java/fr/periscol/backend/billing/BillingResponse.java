package fr.periscol.backend.billing;

public class BillingResponse {

    private float daycareCost;
    private float canteenCost;
    private float totalCost;

    public float getDaycareCost() {
        return daycareCost;
    }

    public void setDaycareCost(float daycareCost) {
        this.daycareCost = daycareCost;
    }

    public float getCanteenCost() {
        return canteenCost;
    }

    public void setCanteenCost(float canteenCost) {
        this.canteenCost = canteenCost;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }
}
