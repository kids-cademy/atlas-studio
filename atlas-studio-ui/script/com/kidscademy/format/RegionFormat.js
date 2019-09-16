$package("com.kidscademy.format");

com.kidscademy.format.RegionFormat = function () {
};

com.kidscademy.format.RegionFormat.prototype = {
	templates: {
		ALL: "%s",
		CENTRAL: "Central %s",
		NORTH: "Northern %s",
		NORTH_EAST: "Northeastern %s",
		EAST: "Eastern %s",
		SOUT_EAST: "Southeastern %s",
		SOUTH: "Southern %s",
		SOUTH_WEST: "Southwestern %s",
		WEST: "Western %s",
		NORTH_WEST: "Northwestern %s"
	},

	format: function (region) {
		const template = this.templates[region.area];
		return $format(template, region.name);
	},

	parse: function (value) {
		return null;
	},

	test: function (value) {
		return !!value;
	},

	toString: function () {
		return "com.kidscademy.format.RegionFormat";
	}
};
$extends(com.kidscademy.format.RegionFormat, Object);
