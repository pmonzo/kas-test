package kas.data.api.v1;

import java.util.Map;

import org.barcelona.opendata.api.v3.BarcelonaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kas.PackageResourceParamatersFactory;
import kas.PackageResourceParameters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PublicationController {

	public static final String API_V1_PACKAGES = "/api/v1/packages";
	
	private final BarcelonaClient client;
	
    public PublicationController(BarcelonaClient client) {
        this.client = client;
    }
    @GetMapping(API_V1_PACKAGES)
    public Flux<Publication> all(@RequestParam Map<String, String> queryMap) {
    	PackageResourceParameters params = PackageResourceParamatersFactory.build(null, queryMap);
        return client.getResults(params);
    }
    @GetMapping(API_V1_PACKAGES + "/{id}")
	    Mono<Publication> findById(@PathVariable String id, @RequestParam Map<String, String> queryMap) {
    	PackageResourceParameters params = PackageResourceParamatersFactory.build(id, queryMap);
        return client.getResult(params);
	}
}
