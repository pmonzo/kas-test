package kas;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.barcelona.opendata.api.v3.BarcelonaClient;
import org.barcelona.opendata.api.v3.PackageSearch;
import org.junit.Test;

import kas.data.api.v1.Publication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TestBarcelonaClient {

	@Test
	public void testConnection(){
		BarcelonaClient client = new BarcelonaClient();
		PackageResourceParameters params = new PackageResourceParameters();
		params.sorts = new ArrayList<String>();
		params.start = 0;
		params.rows = 10;
		Mono<PackageSearch> results = client.getResponse(params);
		assertEquals(true, results.block().getSuccess());
	}
	
	@Test
	public void testResultId(){
		BarcelonaClient client = new BarcelonaClient();
		PackageResourceParameters params = PackageResourceParamatersFactory.build("10", "0", "c1f07915-2d81-46fd-b394-dcb5db5119a2");
		Mono<Publication> response = client.getResult(params, "ca");
		assertEquals("/api/v1/publications/c1f07915-2d81-46fd-b394-dcb5db5119a2", response.block().getHref());
	}
	
	@Test
	public void testResults(){
		BarcelonaClient client = new BarcelonaClient();
		PackageResourceParameters params = PackageResourceParamatersFactory.build("10", "0", null);
		Flux<Publication> result = client.getResults(params, "ca");
		assertEquals(10, result.collect(Collectors.toList()).block().size());
	}
	
	@Test
	public void testResultsPaged(){
		
		PackageResourceParameters params = PackageResourceParamatersFactory.build("20", "2", null);
		BarcelonaClient client = new BarcelonaClient();
		Flux<Publication> result = client.getResults(params, "ca");
		List<Publication> publications = result.collect(Collectors.toList()).block();
		assertEquals(20, publications.size());
	}
}
