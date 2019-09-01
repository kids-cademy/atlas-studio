WinMain.on("load", function() {
	var relatedControl = WinMain.doc.getByClass(com.kidscademy.atlas.RelatedControl);

	const formPage = {
		getObject : function() {
			return {
				category : "WOODWIND"
			};
		}
	};

	relatedControl.onCreate(formPage);
	relatedControl.onStart();

	relatedControl.setValue([ {
		name : "bucium",
		relevance : 0
	}, {
		name : "didgeridoo",
		relevance : 0
	} ]);
});
