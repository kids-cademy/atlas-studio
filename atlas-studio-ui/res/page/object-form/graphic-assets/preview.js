class Page {
	constructor(doc) {
		this._form = doc.getByTag("form");
		this._object = null;
	}
	
	setObject(object) {
		this._object = object;
		this._form.setObject(object);
	}
	
	getObject() {
		return this._object;
	}
	
	getAtlasItem() {
		return this._object;
	}
};

WinMain.on("load", function() {
	const page = new Page(WinMain.doc);	
	const graphicAssets = WinMain.doc.getByClass(com.kidscademy.form.GraphicAssets);
	
	const objectId = Number(WinMain.url.parameters.object);
	if(objectId) {
		AtlasService.getAtlasObject(objectId, object => {
			graphicAssets.onCreate(page);
			page.setObject(object);
		});
	}
});
