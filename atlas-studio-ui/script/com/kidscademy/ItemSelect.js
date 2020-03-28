$package("com.kidscademy");

com.kidscademy.ItemSelect = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._callback = null;

		this._listView = this.getByCssClass("list-view");
		this._listView.on("click", this._onClick, this);

		this.getByCssClass("close").on("click", this._onClose, this);
	}

	load(items) {
		this._listView.setObject(items).show();
	}

	open(callback) {
		this._callback = callback;
		this.show();
	}

	_onClick(ev) {
		const item = ev.target.getParentByCssClass("item");
		if (item != null && this._callback) {
			this._callback(item.getUserData());
			this.hide();
		}
	}

	_onClose() {
		this.hide();
	}

	toString() {
		return "com.kidscademy.ItemSelect";
	}
};
