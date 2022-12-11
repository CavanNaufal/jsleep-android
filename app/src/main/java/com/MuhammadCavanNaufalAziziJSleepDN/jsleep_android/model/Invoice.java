package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model;

public class Invoice extends Serializable{
    public int buyerId;
    public int renterId;

    public PaymentStatus status = PaymentStatus.WAITING;
    public RoomRating rating = RoomRating.NONE;

        public enum PaymentStatus
        {
            FAILED, WAITING, SUCCESS
        }

        public enum RoomRating
        {
            NONE, BAD, NEUTRAL, GOOD
        }
}
