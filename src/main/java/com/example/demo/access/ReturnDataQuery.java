package com.example.demo.access;

import java.util.List;

public class ReturnDataQuery<T> {

    private List<T> query;

    public  T getSington() throws Exception{
        if (query.size() == 0) {
            throw new Exception("查询结果没有元素");
        }
        if (query.size()>1) {
            throw new Exception("查询结果超过一个元素");
        }
        return query.iterator().next();

    }

    public  List<T> getResult() {

        return query;

    }

    public List<T> getQuery() {
        return query;
    }

    public void setQuery(List<T> query) {
        this.query = query;
    }
}
