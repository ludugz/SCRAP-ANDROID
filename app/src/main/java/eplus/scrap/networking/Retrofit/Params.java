package eplus.scrap.networking.Retrofit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Params {

    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("items_per_page")
    @Expose
    private Integer itemsPerPage;
    @SerializedName("total_items")
    @Expose
    private Integer totalItems;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

}