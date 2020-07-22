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
	 * @param {Node} node built-in {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this.addCssClass("exclude");

		this._object = null;

		/**
		 * Force optional control as mandatory for a single validity test. Usable when a control is optional 
		 * most of the time but in a particular case need to be mandatory. This list is temporary and is used
		 * in conjuction with {@link #isValid()} method.
		 * <pre>
		 * 		if(form.mandatory("source").isValid()) { ... }
		 * </pre>  
		 */
		this._mandatory = [];

		/**
		 * Iterable for controls owned by this form data.
		 * @type {js.dom.ControlsIterable}
		 */
		this._controls = new js.dom.ControlsIterable(this);
	}

	open() {
		this.reset();
		this.show();
	}

	isValid(includeOptional = false) {
		var valid = true;
		this._controls.forEach(control => {
			if (control.isVisible()) {
				valid = control.isValid(includeOptional || this._mandatory.includes(control.getName())) && valid;
			}
		});
		this._mandatory = [];
		return valid;
	}

	getFormData() {
		const formData = new FormData();
		this._controls.forEach(control => formData.append(control.getName(), control.getValue()));
		return formData;
	}

	setObject(object) {
		this._object = object;
		this._controls.forEach(control => {
			const opp = this._getOPPath(control);
			if (opp !== null) {
				const value = js.lang.OPP.get(object, opp);
				if (typeof value !== "undefined") {
					control.setValue(value);
				}
			}
		});
		return this;
	}

	getObject(object) {
		if (!object) {
			object = this._object;
		}
		if (!object) {
			object = {};
		}

		this._controls.forEach(control => {
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
		this._controls.forEach(control => control.reset());
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
		if (typeof controlName === "undefined") {
			return super.focus();
		}
		this.getByName(controlName).focus();
		return this;
	}

	show(...controlNames) {
		if (controlNames.length === 0) {
			return super.show();
		}
		if (controlNames.length === 1 && typeof controlNames[0] === "boolean") {
			return super.show(controlNames[0]);
		}

		controlNames.forEach(controlName => {
			// see hide method comments
			const control = this._getControl(controlName).show();
			const controlset = control.getParentByCssClass("controlset");
			if (controlset != null) {
				controlset.show();
			}
		});
		return this;
	}

	hide(...controlNames) {
		if (controlNames.length === 0) {
			return super.hide();
		}

		controlNames.forEach(controlName => {
			// need to hide both control and its parent set, if exists
			// control should be hide to exclude from form validation
			// if control set is not hidden control label is visible on UI
			const control = this._getControl(controlName).hide();
			const controlset = control.getParentByCssClass("controlset");
			if (controlset != null) {
				controlset.hide();
			}
		});
		return this;
	}

	/**
	 * Enable named select options, less those from given excludes list. If exludes argument is missing it defaults to
	 * empty array and all options are enabled. Excludes list is compared with option text.
	 * 
	 * @param {String} selectName select control name,
	 * @param {Array} excludes optional excludes list, default to empty array.
	 */
	enableOptions(selectName, excludes = []) {
		const select = this._getControl(selectName);
		$assert(select.getTag() === "select", "com.kidscademy.FormData#disableOptions", "Named control is not a select.");
		select.getChildren().forEach(option => option._node.disabled = excludes.includes(option.getText()));
	}

	mandatory(...controlNames) {
		this._mandatory = controlNames;
		return this;
	}

	_getOPPath(control) {
		const name = control.getName();
		return name !== null ? js.util.Strings.toScriptCase(name) : null;
	}

	_getControl(controlName) {
		const control = this.getByName(controlName);
		if (control == null) {
			throw `Control |${controlName}| not found.`;
		}
		return control;
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