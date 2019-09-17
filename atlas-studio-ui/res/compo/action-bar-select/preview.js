WinMain.on("load", function() {
	var taxonomySelect = WinMain.doc.getByName("taxonomy");
	taxonomySelect.setOptions([ {
		code : "Kingdom",
		name : "Animalia"
	}, {
		code : "Phylum",
		name : "Chordata"
	}, {
		code : "Class",
		name : "Mammalia"
	}, {
		code : "Order",
		name : "Carnivora"
	}, {
		code : "Suborder",
		name : "Caniformia"
	}, {
		code : "Family",
		name : "Urside"
	}, {
		code : "Genus",
		name : "Ursus"
	}, {
		code : "Species",
		name : "Ursus arctos"
	}, {
		code : "Subscpecies",
		name : "Ursus arctos horribilis"
	} ]);
});