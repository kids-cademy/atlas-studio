$package("com.kidscademy.form");

com.kidscademy.form.LinkSelect = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this.on("click", this._onClick, this);
	}

	open(links, callback) {
		super.setObject(links);
		this._callback = callback;
		this.show();
	}

	close() {
		this.hide();
	}

	_onClick(ev) {
		const li = ev.target.getParentByTag("li");
		if (li) {
			this._callback(li.getUserData());
			this.hide();
		}
	}

	toString() {
		return "com.kidscademy.form.LinkSelect";
	}
};
