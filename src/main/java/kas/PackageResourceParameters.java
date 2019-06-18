package kas;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PackageResourceParameters {

	public List<String> sorts;
	public Integer start;
	public Integer rows;
	public String q;
	
	public PackageResourceParameters() {
		
	}
	
	public String query() {
		StringBuilder sb = new StringBuilder("?");
		if(start != null) {
			sb.append("start=").append(this.start);
		}
		if(rows != null) {
			sb.append("&rows=").append(rows);
		}
		if(StringUtils.isNotEmpty(q)) {
			sb.append("&q=").append(q);
		}
		
		return sb.toString();
	}
}
