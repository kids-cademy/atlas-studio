AtlasService.getCollectionItems = function(collectionName, callback) {
	var items = [ {
		id : 1,
		name : "accordion",
		display : "Accordion",
		iconSrc : "/media/atlas/instrument/accordion/icon_96x96.jpg"
	}, {
		id : 2,
		name : "alphorn",
		display : "Alphorn",
		iconSrc : "/media/atlas/instrument/alphorn/icon_96x96.jpg"
	}, {
		id : 3,
		name : "bagpipes",
		display : "Bagpipes",
		iconSrc : "/media/atlas/instrument/bagpipes/icon_96x96.jpg"
	}, {
		id : 4,
		name : "balaban",
		display : "Balaban",
		iconSrc : "/media/atlas/instrument/balaban/icon_96x96.jpg"
	}, {
		id : 5,
		name : "balafon",
		display : "Balafon",
		iconSrc : "/media/atlas/instrument/balafon/icon_96x96.jpg"
	} ];

	return callback(items);
};
