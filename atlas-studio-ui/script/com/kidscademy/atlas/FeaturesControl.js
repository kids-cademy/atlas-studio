$package("com.kidscademy.atlas");

com.kidscademy.atlas.FeaturesControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
	}

	setValue(classification) {
	}

	getValue() {
	}

	isValid() {
		return true;
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.FeaturesControl";
	}
};

$preload(com.kidscademy.atlas.FeaturesControl);
