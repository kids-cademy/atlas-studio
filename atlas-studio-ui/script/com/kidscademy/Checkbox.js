$package("com.kidscademy");

/**
 * Checkbox class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.Checkbox = class extends js.dom.Element {
    /**
	 * Construct an instance of Checkbox class.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this._checkbox = this.getByCssClass("control").on("click", this._onClick, this);
	}

	setObject(state) {
		this._checkbox.addCssClass("checked", state === "PUBLISHED");
	}
	
	/**
	 * Check this checkbox. After this method execution {@link #checked} returns true.
	 * 
	 * @return com.kidscademy.Checkbox this object.
	 */
	check() {
		this._checkbox.addCssClass("checked");
		return this;
	}

	/**
	 * Uncheck this checkbox. After this method execution {@link #checked} returns false.
	 * 
	 * @return com.kidscademy.Checkbox this object.
	 */
	uncheck() {
		this._checkbox.removeCssClass("checked");
		return this;
	}

	reset() {
		return this.uncheck();
	}

	/**
	 * Test if this checkbox is selected. Returns true if this checkbox is selected or false otherwise.
	 * 
	 * @return Boolean this checkbox state.
	 */
	checked() {
		return this._checkbox.hasCssClass("checked");
	}

	getName() {
		return this.getAttr("data-name");
	}

	getValue() {
		return this.getAttr("data-value");
	}


	/**
	 * Override control validation. Simply returns true since a checkbox is always valid.
	 * 
	 * @return Boolean always returns true.
	 */
	isValid() {
		return true;
	}

	/**
	 * Click event handler takes care to toggle checked state.
	 * 
	 * @param js.event.Event ev mouse click event.
	 */
	_onClick(ev) {
		this._checkbox.toggleCssClass("checked");
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.Checkbox";
	}
};

$preload(com.kidscademy.Checkbox);
