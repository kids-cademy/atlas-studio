$package("com.kidscademy.reader");

com.kidscademy.reader.ConservationView = class extends js.dom.Element {
	/**
	 * Construct reader related objects view.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this._activeItem = null;
	}

	setObject(conservation) {
		if (!conservation) {
			this.hide();
			return;
		}
		this.show();

		if (this._activeItem != null) {
			this._activeItem.removeCssClass("active");
		}
		this._activeItem = this.getByCssClass(conservation);
		if (this._activeItem != null) {
			this._activeItem.addCssClass("active");
		}
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.reader.ConservationView";
	}
};
