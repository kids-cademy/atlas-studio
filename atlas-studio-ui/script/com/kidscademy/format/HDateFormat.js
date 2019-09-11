$package("com.kidscademy.format");

com.kidscademy.format.HDateFormat = function() {
};

com.kidscademy.format.HDateFormat.Format = [ "DATE", "YEAR", "DECADE", "CENTURY", "MILLENNIA", "KYA", "MYA", "BYA" ];

com.kidscademy.format.HDateFormat.Period = [ "FULL", "BEGINNING", "MIDDLE", "END" ];

com.kidscademy.format.HDateFormat.Era = [ "CE", "BCE" ];

com.kidscademy.format.HDateFormat.prototype = {
	format : function(hdate) {

		switch (this.getFormat(hdate)) {
		case "YEAR":
			return $format("%d %s", hdate.value, this.getEra(hdate));

		case "CENTURY":
			return $format("%d %s", (hdate.value - 1) * 100, this.getEra(hdate));

		default:
			break;
		}

		return "";
	},

	getFormat : function(hdate) {
		if (!hdate.mask) {
			return null;
		}
		return com.kidscademy.format.HDateFormat.Format[hdate.mask & 0x000000FF];
	},

	getEra : function(hdate) {
		if (!hdate.mask) {
			return null;
		}
		return com.kidscademy.format.HDateFormat.Era[(hdate.mask & 0x00FF0000) >> 16];
	},

	parse : function(value) {
		return null;
	},

	test : function(value) {
		return !!value;
	},

	toString : function() {
		return "com.kidscademy.format.HDateFormat";
	}
};
$extends(com.kidscademy.format.HDateFormat, Object);
