package kas;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;

public class PackageResourceParamatersFactory {

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static PackageResourceParameters build(ServerRequest request) {

		String rowsRequest = request.queryParams().getFirst("rows");
		String pageRequest = request.queryParams().getFirst("page");
		String id = request.pathVariable("id");

		return build(rowsRequest, pageRequest, id);
	}

	public static PackageResourceParameters build(String id, Map<String, String> queryMap) {

		String rowsRequest = queryMap.get("rows");
		String pageRequest = queryMap.get("page");

		return build(rowsRequest, pageRequest, id);
	}
	
	public static PackageResourceParameters build(String rowsRequest, String pageRequest, String id) {
		PackageResourceParameters params = new PackageResourceParameters();
		params.sorts = new ArrayList<String>();
		params.rows = Integer.valueOf(StringUtils.defaultString(rowsRequest, "10"));
		Integer page = Integer.valueOf(StringUtils.defaultString(pageRequest, "0"));
		params.start = page * params.rows;
		params.q = (id != null)?"id:"+id:null;

		return params;
	}
}
