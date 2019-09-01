WinMain.on("load", function() {
	var factsControl = WinMain.doc.getByClass(com.kidscademy.atlas.FactsControl);
	factsControl.setValue({
		"First played on Carnegie Hall in 1939" : "The first to play the accordion in New York City's famous Carnegie Hall were Gene von Halberg, Abe Goldman, and Joe Biviano, in 1939.",
		"Most parts are hand made" : "Accordions are still made with a lot of human hands as opposed to by machinery. Some parts are made by machine but the best are mostly hand made by craftsman.",
		"Golden Age of the Accordion" : "The 1900s to the 1960s has been referred to as the 'Golden Age of the Accordion'.",
		"Pietro Deiro is the 'Daddy of the Accordion'" : "Pietro Deiro, an Italy-born accordionist was known as the 'Daddy of the Accordion'. He had a career in San Francisco during the vaudeville era and was even signed to RCA Victor Records.",
		"Can generate long duration sounds" : "The accordion is able to sustain sound for a much longer time than most other instruments"
	});
});