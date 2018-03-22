package eplus.scrap.model;

/**
 * Created by nals-anhdv on 8/14/17.
 */

public class Pagination {

    /**
     * page : 1
     * items_per_page : 10
     * total_items : 29
     */

    private int page;
    private int items_per_page;
    private int total_items;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItems_per_page() {
        return items_per_page;
    }

    public void setItems_per_page(int items_per_page) {
        this.items_per_page = items_per_page;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }
}
