$package("com.kidscademy.format");

com.kidscademy.format.RegionFormat = class {
	constructor() {
		this.templates = com.kidscademy.format.RegionFormat.Templates;
	}

	format(region) {
		const template = this.templates[region.area];
		if (!region.less) {
			return $format(template, region.name);
		}
		const lessTemplate = this.templates[region.lessArea];
		return $format(`${template} less ${lessTemplate}`, region.name, region.less);
	}

	parse(value) {
		return null;
	}

	test(value) {
		return !!value;
	}

	toString() {
		return "com.kidscademy.format.RegionFormat";
	}
};

com.kidscademy.format.RegionFormat.Templates = {
	ALL: "%s",
	CENTRAL: "Central %s",
	NORTH: "Northern %s",
	NORTH_EAST: "Northeastern %s",
	EAST: "Eastern %s",
	SOUTH_EAST: "Southeastern %s",
	SOUTH: "Southern %s",
	SOUTH_WEST: "Southwestern %s",
	WEST: "Western %s",
	NORTH_WEST: "Northwestern %s"
};
