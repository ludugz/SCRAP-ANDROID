package eplus.scrap.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nals-anhdv on 8/16/17.
 */
@Table(name = "Tickets")
public class TicketDAO extends Model {

    @Column(name = "Product_name")
    public String product_name;
    @Column(name = "Product_id")
    public int product_id;
    @Column(name = "Item_id")
    public long item_id;
    @Column(name = "Order_id")
    public int order_id;
    @Column(name = "Price")
    public int price;
    @Column(name = "Amount")
    public int amount;
    @Column(name = "Image_path")
    public String image_path;
    @Column(name = "Relative_path")
    public  String relative_path;
    @Column(name = "Qr_code")
    public String qr_code;
    @Column(name = "Venue")
    public String venue;
    @Column(name = "Date_time")
    public String date_time;
    @Column(name = "Event_start")
    public String event_start;
    @Column(name = "Event_end")
    public String event_end;
    @Column(name = "Scanning_end")
    public String scanning_end;
    @Column(name = "Scanning_start")
    public String scanning_start;
    @Column(name = "Status")
    public int status;
    @Column(name = "Order_status")
    public String order_status;
    @Column(name = "Scanapp_status")
    public String scanapp_status;
    @Column(name = "Seat")
    public String seat;



    public TicketDAO(){
        super();
    }

    public TicketDAO(Ticket ticket ){
        super();
        this.product_id = ticket.getProduct_id();
        this.product_name = ticket.getProduct_name();
        this.order_id = ticket.getOrder_id();
        this.qr_code = ticket.getQr_code();
        this.venue =  ticket.getVenue();
        this.date_time = ticket.getDate_time();
        this.event_start = ticket.getEvent_start();
        this.event_end = ticket.getEvent_end();
        this.scanning_end = ticket.getScanning_end();
        this.status = ticket.getStatus();
        this.image_path = ticket.getImage_path();
        this.scanapp_status = ticket.getScanapp_status();
        this.order_status = ticket.getOrder_status();
        this.seat = ticket.getSeat();
        this.amount = ticket.getAmount();
        this.scanning_start = ticket.getScanning_start();


    }


}
