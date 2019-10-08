WinMain.on("load", function() {
	var atlasObject = {
		taxonomy : [ {
			name : "Kingdom",
			value : "Animalia"
		}, {
			name : "Phylum",
			value : "Chordata"
		}, {
			name : "Class",
			value : "Mammalia"
		}, {
			name : "Order",
			value : "Carnivora"
		}, {
			name : "Suborder",
			value : "Feliformia"
		}, {
			name : "Family",
			value : "Felidae"
		}, {
			name : "Genus",
			value : "Panthera"
		}, {
			name : "Species",
			value : "Panthera leo"
		} ],
		aliases : [ "Panthera", "Felidae" ],
		spreading : [ {
			name : "India",
			area : "WEST"
		}, {
			name : "Sub-Saharan Africa",
			area : "ALL"
		} ],
		startDate : {
			value : 112.03,
			mask : 132102
		},
		endDate : {
			value : 93.5,
			mask : 132102
		},
		conservation: null
	};

	var objectView = WinMain.doc.getByCss(".object-view");
	objectView.setObject(atlasObject);
});
