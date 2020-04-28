WinMain.on("load", () => {
	const form = WinMain.doc.getByTag("form");
	const iconControl = WinMain.doc.getByClass(com.kidscademy.IconControl);

	const collectionId = Number(WinMain.url.parameters.collection);
	if(collectionId) {
		AtlasService.getCollection(collectionId, collection => {
			iconControl.config({
				object: collection,
				imageKind: "COLLECTION"
			});
			
			form.setObject(collection);
		});
	}
});
