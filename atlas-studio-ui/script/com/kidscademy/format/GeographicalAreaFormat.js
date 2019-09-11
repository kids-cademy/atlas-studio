$package("com.kidscademy.format");

com.kidscademy.format.GeographicalAreaFormat = function() {
};

com.kidscademy.format.GeographicalAreaFormat.prototype = {
	format : function(area) {
		return area.name;
	},

	parse : function(value) {
		return null;
	},

	test : function(value) {
		return !!value;
	},

	toString : function() {
		return "com.kidscademy.format.GeographicalAreaFormat";
	}
};
$extends(com.kidscademy.format.GeographicalAreaFormat, Object);
