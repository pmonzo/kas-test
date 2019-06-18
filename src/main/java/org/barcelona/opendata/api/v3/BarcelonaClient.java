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
	
	/**
	 * Escoger la url correcta segun el idioma recibido
	 * @param res Elemento recibido del servicio rest del ayuntamiento de Barcelona
	 * @param lang Idioma indicado en las cabeceras, por defecto catalan
	 * @return
	 */
	private String calculateUrl(Result_ res, String lang) {
		String url;
		if("ca".equals(lang)) {
			url = res.getUrlTornada().getCa();
		} else if("es".equals(lang)) {
			url = res.getUrlTornada().getEs();
		} else if("es".equals(lang)) {
			url = res.getUrlTornada().getEn();
		} else {
			url = res.getUrlTornada().getCa();
		}
		return url;
	}
	
	public Flux<Publication> getResults(PackageResourceParameters params, String lang) {

		Flux<Publication> collect = getResponse(params)
		.map(x -> x.getResult().getResults().stream()).flatMapMany(Flux::fromStream)
		.map(res -> new Publication(res.getCode(), res.getOrganization().getDescription(), calculateUrl(res, lang), res.getId()));

		return collect;
	}
	
	public Mono<Publication> getResult(PackageResourceParameters params, String lang) {

		Mono<Publication> collect = getResults(params, lang).next();

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
