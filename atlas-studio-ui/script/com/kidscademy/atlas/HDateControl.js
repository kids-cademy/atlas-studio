$package("com.kidscademy.atlas");

com.kidscademy.atlas.HDateControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._valueInput = this.getByName("value");
		this._formatSelect = this.getByName("format");
		this._periodSelect = this.getByName("period");
		this._eraSelect = this.getByName("era");
	}

	setValue(hdate) {
		if (hdate == null) {
			this._valueInput.reset();
			this._formatSelect.reset();
			this._periodSelect.reset();
			this._eraSelect.reset();
			return;
		}

		this._valueInput.setValue(hdate.value);
		this._formatSelect.setValue(hdate.mask & 0x000000FF);
		this._periodSelect.setValue(hdate.mask & 0x0000FF00);
		this._eraSelect.setValue(hdate.mask & 0x00FF0000);
	}

	getValue() {
		const value = this._valueInput.getValue();
		if (value == null) {
			return null;
		}
		const format = this._formatSelect.getValue();
		if (format == null) {
			return null;
		}
		const period = this._periodSelect.getValue();
		if (period == null) {
			return null;
		}
		const era = this._eraSelect.getValue();
		if (era == null) {
			return null;
		}

		return {
			value: Number(value),
			mask: Number(format) + Number(period) + Number(era)
		};
	}

	isValid() {
		var valid = this._valueInput.isValid();
		valid = this._formatSelect.isValid() && valid;
		valid = this._periodSelect.isValid() && valid;
		valid = this._eraSelect.isValid() && valid;
		return valid;
	}

	reset() {
		this._valueInput.reset();
		this._formatSelect.reset();
		this._periodSelect.reset();
		this._eraSelect.reset();
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.HDateControl";
	}
};

$preload(com.kidscademy.atlas.HDateControl);
