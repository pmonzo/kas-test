package org.barcelona.opendata.api.v3;

import java.util.List;

import kas.data.api.v1.Publication;

public class PackageResultResponse {

	public Integer count;
	public String sort;
	public List<String> facets;
	public List<Publication> results;
	
	public PackageResultResponse() {}
}
