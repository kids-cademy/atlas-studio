WinMain.on("load", function() {
	var linksControl = WinMain.doc.getByClass(com.kidscademy.form.LinksControl);

	const formPage = {
		getCollection : function() {
			return {
				id : 1
			}
		},

		getObject : function() {
			return {
				display : "Cajon"
			};
		}
	};
	linksControl.onCreate(formPage);

	linksControl.setValue([ {
		id : 816,
		url : "http://www.softschools.com/facts/music_instruments/cajon_facts/2999/",
		domain : "softschools.com",
		display : "Soft Schools",
		definition : "Facts about cajon on Soft Schools.",
		iconSrc : "/media/link/softschools.png",
		linkSource : {
			apis : "description,facts"
		}
	} ]);
});