$package("com.kidscademy.form");

com.kidscademy.form.FeaturesControl = class extends js.dom.Control {
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
		return "com.kidscademy.form.FeaturesControl";
	}
};

$preload(com.kidscademy.form.FeaturesControl);
