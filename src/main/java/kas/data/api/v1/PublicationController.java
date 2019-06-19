package kas.data.api.v1;

import java.util.Map;

import org.barcelona.opendata.api.v3.BarcelonaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;
import kas.PackageResourceParamatersFactory;
import kas.PackageResourceParameters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador para gestionar las peticiones de información contra
 * https://opendata-ajuntament.barcelona.cat/data/api/3/action/package_search
 * 
 * 
 * @author pedro.monzo
 *
 */
@RestController
public class PublicationController {

	public static final String API_V1_PACKAGES = "/api/v1/publications";
	
	private final BarcelonaClient client;
	
    public PublicationController(BarcelonaClient client) {
        this.client = client;
    }
            
    @Timed(value = "publication.all")
    @GetMapping(API_V1_PACKAGES)
    public Flux<Publication> all(@RequestParam Map<String, String> queryMap, @RequestHeader(value="Accept-Language") String lang) {
    	PackageResourceParameters params = PackageResourceParamatersFactory.build(null, queryMap);
        return client.getResults(params, lang);
    }
    
    @Timed(value = "publication.findById")
    @GetMapping(API_V1_PACKAGES + "/{id}")
	public Mono<Publication> findById(@PathVariable String id, @RequestParam Map<String, String> queryMap, @RequestHeader(value="Accept-Language") String lang) {
    	lang = procesarAcceptLanguage(lang);
    	PackageResourceParameters params = PackageResourceParamatersFactory.build(id, queryMap);
        return client.getResult(params,lang);
	}
    
    /**
     * Calcula el idioma solicitado teniendo en cuenta que los idiomas de la peticion
     * estan ordenados por prioridad.     * 
     * @param lang
     * @return
     */
    private String procesarAcceptLanguage(String lang) {
    	int indexOfEs = convertirIndexOf(lang.indexOf("es"));
    	int indexOfCa = convertirIndexOf(lang.indexOf("ca"));
    	int indexOfEn = convertirIndexOf(lang.indexOf("en"));
    	if (indexOfCa > indexOfEs && indexOfEn > indexOfEs) {
    		return "es";
    	} else if (indexOfCa > indexOfEn && indexOfEs > indexOfEn) {
    		return "en";
    	} else {
    		return "ca";
    	}
    }
    
    private int convertirIndexOf(int i) {
    	return (i < 0)?9999: i;
    }
}
