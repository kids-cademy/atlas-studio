WinMain.on("load", () => {
	const collection = {
			taxonomyMeta: [{
				name: "kingdom"
			}, {
				name: "phylum"
			}, {
				name: "class"
			}, {
				name: "order"
			}, {
				name: "family"
			}, {
				name: "genus"
			}, {
				name: "species" 
			}, {
				name: "variety" 
			}]
	}
	
	const formPage = {
			getCollection: function() {
				return collection;
			}
	};
	
	const taxonomyView = WinMain.doc.getByClass(com.kidscademy.form.TaxonomyControl);
	taxonomyView.onCreate(formPage);
	taxonomyView.setValue(null);
});