$package("com.kidscademy.atlas");

com.kidscademy.atlas.AliasesControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
	}

	setValue(aliases) {
		super.setValue(aliases.join(", "));
	}

	getValue() {
		var value = super.getValue();
		if (value == null) {
			return [];
		}
		return value.trim().split(/\s*,\s*/);
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.AliasesControl";
	}
};

$preload(com.kidscademy.atlas.AliasesControl);
