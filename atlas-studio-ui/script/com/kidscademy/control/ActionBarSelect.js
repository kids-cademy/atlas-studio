$package("com.kidscademy.control");

/**
 * Abstract select integrated into action bars. This select supports options displayed on multiple columns. Concrete
 * component need to define options columns.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.control.ActionBarSelect = class extends js.dom.Control {
	/**
 	 * Construct an instance of ActionBarSelect class.
	 * 
 	 * @param {js.dom.Document} ownerDoc element owner document,
 	 * @param {Node} node native {@link Node} instance.
 	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._input = this.getByCssClass("abs-input");

		this._options = this.getByCssClass("abs-options");
		this._options.on("change", this._onOptionsChange, this);

		this._icon = this.getByCssClass("abs-icon");
		this._icon.on("click", this._onIconClick, this);

		this._events = this.getCustomEvents();
		this._events.register("change");
	};

	setOptions(options) {
		this._options.load(options);
	}

	/**
	 * Remove input content and hide options.
	 */
	reset() {
		this._input.reset();
		this._options.hide();
	}

	/**
	 * Handle mouse click event on arrow icon. Toggle options visibility.
	 * 
	 * @param {js.event.Event} ev mouse click event.
	 */
	_onIconClick(ev) {
		if (this._options.isVisible()) {
			this._options.hide();
		}
		else {
			this._options.show();
		}
	}

	/**
	 * On option selected display data value on this select input and fire 'change' event.
	 * @param {Object} data custom data stored by option columns.
	 */
	_onOptionsChange(data) {
		this._input.setValue(data.value);
		this._options.hide();
		this._events.fire("change", data);
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.control.ActionBarSelect";
	}
};

com.kidscademy.control.ActionBarSelect.Options = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this._list = this.getByCssClass("list");
		this.on("click", this._onClick, this);

		this._events = this.getCustomEvents();
		this._events.register("change");
	}

	load(options) {
		this._list.setObject(options);
	}

	_onClick(ev) {
		ev.halt();
		var tr = ev.target.getParentByTag("tr");
		if (tr) {
			this._events.fire("change", tr.getUserData());
		}
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.control.ActionBarSelect.Options";
	}
};
