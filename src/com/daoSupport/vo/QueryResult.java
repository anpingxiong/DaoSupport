package com.daoSupport.vo;

import java.util.List;

public class QueryResult<T> {
    private  List<T>   results;
    private long totalCount;
	
	public List<T> getResults() {
		return results;
	}
	
	public void setResults(List<T> results) {
		this.results = results;
	}
	
	public long getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
}
