$package("com.kidscademy.atlas");

com.kidscademy.atlas.PicturesControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this.setAttr("data-list", ".");

		this.on("click", this._onClick, this);

		this.on("dragstart", this._onDragStart, this);
		this.on("dragover", this._onDragOver, this);
		this.on("drop", this._onDrop, this);

		this._events = this.getCustomEvents();
		this._events.register("picture-selected");
	}

	setValue(pictures) {
		this.setObject(pictures);
		this.show();
	}

	getValue() {
		return this.getChildren().map(element => element.getUserData());
	}

	addPicture(picture) {
		const pictures = this.getValue();
		pictures.push(picture);
		this.setObject(pictures);
	}

	updatePicture(picture) {
		const element = this.findChild(element => element.getUserData().fileName === picture.fileName);
		element.setObject(picture);
	}

	removePicture(picture) {
		const element = this.findChild(element => element.getUserData().fileName === picture.fileName);
		element.remove();
	}

	isValid() {
		return true;
	}

	_onClick(ev) {
		const item = ev.target.getParentByCssClass("item");
		if (item) {
			this._events.fire("picture-selected", item.getUserData());
		}
	}

	_onDragStart(ev) {
		const li = ev.target.getParentByTag("li");
		ev.setData("index", li.getChildIndex());
	}

	_onDragOver(ev) {
		ev.prevent();
	}

	_onDrop(ev) {
		ev.prevent();
		const source = this.getByIndex(ev.getData("index"));
		var target = ev.target.getParentByTag("li");
		if (target == null) {
			return;
		}

		// if ctrl key is pressed insert after drop element
		if (ev.ctrlKey) {
			target = target.getNextSibling();
			// special case when ask to insert after last child element
			if (target == null) {
				this.addChild(source);
				return;
			}
		}

		target.insertBefore(source);
	}

	toString() {
		return "com.kidscademy.atlas.PicturesControl";
	}
};
