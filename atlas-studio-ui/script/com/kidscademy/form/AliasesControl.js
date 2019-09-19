$package("com.kidscademy.form");

com.kidscademy.form.AliasesControl = class extends js.dom.Control {
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
		return "com.kidscademy.form.AliasesControl";
	}
};

$preload(com.kidscademy.form.AliasesControl);
