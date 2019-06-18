package org.barcelona.opendata.api.v3;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode;

import kas.PackageResourceParameters;
import kas.data.api.v1.Publication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BarcelonaClient {

	private final String baseUrl = "https://opendata-ajuntament.barcelona.cat";
	private static WebClient client;

	public BarcelonaClient() {
		if(client == null){
			DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
			factory.setEncodingMode(EncodingMode.TEMPLATE_AND_VALUES);
			client = WebClient.builder().uriBuilderFactory(factory).build();
		}
	}
	
	public Flux<Publication> getResults(PackageResourceParameters params) {

		Flux<Publication> collect = getResponse(params)
		.map(x -> x.getResult().getResults().stream()).flatMapMany(Flux::fromStream)
		.map(res -> new Publication(res.getCode(), res.getOrganization().getDescription(), res.getUrlTornada().getCa(), res.getId()));

		return collect;
	}
	
	public Mono<Publication> getResult(PackageResourceParameters params) {

		Mono<Publication> collect = getResults(params).next();

		return collect;
	}

	public Mono<PackageSearch> getResponse(PackageResourceParameters params) {
		Mono<ClientResponse> result = client.get()
			.uri(apiUrl(params))
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange();
		result.flatMap(res -> res.bodyToMono(PackageSearch.class));
		Mono<PackageSearch> pub = result.flatMap(res -> res.bodyToMono(PackageSearch.class));
		return pub;
	}

	private String apiUrl(PackageResourceParameters params) {
		return "/data/api/3/action/package_search" + params.query();
	}
	
}
