$package("com.kidscademy");

/**
 * FormData class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.FormData = class extends js.dom.Element {
	/**
	 * Construct an instance of FormData class.
	 * 
	 * @param {js.dom.Document} ownerDoc element owner document,
	 * @param {Node} node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Iterable for controls owned by this form data.
		 * @type {js.dom.ControlsIterable}
		 */
		this._iterable = new js.dom.ControlsIterable(this);
	}

	open() {
		this.reset();
		this.show();
	}

	isValid(includeOptional = false) {
		var valid = true;
		this._iterable.forEach(control => valid = control.isValid(includeOptional) && valid);
		return valid;
	}

	getFormData() {
		const formData = new FormData();
		this._iterable.forEach(control => formData.append(control.getName(), control.getValue()));
		return formData;
	}

	setObject(object) {
		this._iterable.forEach(control => {
			const opp = this._getOPPath(control);
			if (opp !== null) {
				const value = js.lang.OPP.get(object, opp);
				if (typeof value !== "undefined") {
					control.setValue(value);
				}
			}
		});
	}

	getObject(object) {
		if (typeof object === "undefined") {
			object = {};
		}
		this._iterable.forEach(control => {
			const opp = this._getOPPath(control);
			if (opp !== null) {
				js.lang.OPP.set(object, opp, control.getValue());
			}
		});
		return object;
	}

	setValue(controlName, value) {
		const control = this.getByName(controlName);
		$assert(control != null, "com.kidscademy.FormData#setValue", "Missing control |%s|.", controlName);
		control.setValue(value);
		return this;
	}

	getValue(controlName) {
		const control = this.getByName(controlName);
		$assert(control != null, "com.kidscademy.FormData#setValue", "Missing control |%s|.", controlName);
		return control.getValue();
	}

	reset() {
		this._iterable.forEach(control => control.reset());
		return this.focus();
	}

	disable(controlName) {
		this.getByName(controlName).disable();
		return this;
	}

	enable(controlName) {
		this.getByName(controlName).enable();
		return this;
	}

	focus(controlName) {
		if(typeof controlName === "undefined") {
			return super.focus();
		}
		this.getByName(controlName).focus();
		return this;
	}

	_getOPPath(control) {
		const name = control.getName();
		return name !== null ? js.util.Strings.toScriptCase(name) : null;
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.FormData";
	}
};

$preload(com.kidscademy.FormData);