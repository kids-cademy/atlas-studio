WinMain.on("load", function() {
	var relatedControl = WinMain.doc.getByClass(com.kidscademy.form.RelatedControl);

	const formPage = {
		getCollection : function() {
			return {
				id : 1,
				taxonomyClass : "MUSICAL_INSTRUMENT"
			};
		},

		getObject : function() {
			return {
				category : "WOODWIND"
			};
		}
	};

	relatedControl.onCreate(formPage);
	relatedControl.onStart(formPage);

	relatedControl.setValue([ "bucium", "didgeridoo", "flute", "violin" ]);
});
