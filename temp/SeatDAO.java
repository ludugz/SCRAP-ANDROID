package eplus.scrap.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nals-anhdv on 8/24/17.
 */
@Table(name = "Seats")
public class SeatDAO extends Model {
    @Column(name = "Order_id")
    public long order_id;
    @Column(name = "Item_id")
    public long item_id;
    @Column(name = "Price")
    public int price;
    @Column(name = "Amount")
    public int amount;
    @Column(name = "Seat")
    public String seat;
    public SeatDAO(){
        super();
    }

//    public SeatDAO(Ticket.SeatsBean seatsBean, long order_id ){
//        super();
//        this.item_id = seatsBean.getItem_id();
//        this.price = seatsBean.getPrice();
//        this.amount = seatsBean.getAmount();
//        this.seat = seatsBean.getSeat();
//        this.order_id = order_id;
//
//    }
}
