package kas.data.api.v1;

public class Publication {

	private String code;
	private String description;
	private String uri;
	private String href;
	
	public Publication() {
		super();
	}

	public Publication(String code, String description, String uri, String id) {
		super();
		this.code = code;
		this.description = description;
		this.uri = uri;
		this.href = PublicationController.API_V1_PACKAGES + "/" + id;
	}
	

	public String getHref() {
		return href;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getUri() {
		return uri;
	}

	
}
