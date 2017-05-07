package edu.ttu.spm.cheapride;

/**
 * Created by Administrator on 2017/5/5.
 */

public class pageable {
    private int page;
    private int pageSize;


    public pageable(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public void setPage(int page){
        this.page = page;
    }

    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }

    public int getPage() {

        return page;
    }

    public int getNextPage() {
        page = page + 1;
        return page;
    }

    public int getPrevPage() {
        if(this.page == 1)
            return page;
        else{
            page = page - 1;
            return page;
        }
    }

    public int getPageSize(){

        return pageSize;
    }
}
