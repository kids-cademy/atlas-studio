$package("com.kidscademy.atlas");

com.kidscademy.atlas.ImagesControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this.setAttr("data-list", ".");

		this.on("click", this._onClick, this);

		this._events = this.getCustomEvents();
		this._events.register("image-selected");
	}

	setValue(images) {
		// on server images are stored on a map with next keys: icon, cover, featured and contextual
		// on user interface images are displayed as a list view

		function addImage(image) {
			if (typeof image !== "undefined") {
				imagesList.push(image);
			}
		}

		var imagesList = [];
		addImage(images.icon);
		addImage(images.cover);
		addImage(images.featured);
		addImage(images.contextual);

		this.setObject(imagesList);
		this.show();
	}

	getValue() {
		var images = {};
		this.getChildren().map(element => {
			const image = element.getUserData();
			images[image.imageKey] = image;
		});
		return images;
	}

	addImage(image) {
		const images = this.getValue();
		images[image.imageKey] = image;
		this.setObject(images);
	}

	updateImage(image) {
		const element = this.findChild(element => element.getUserData().fileName === image.fileName);
		element.setObject(image);
	}

	removeImage(image) {
		const element = this.findChild(element => element.getUserData().fileName === image.fileName);
		element.remove();
	}

	isValid() {
		return true;
	}

	_onClick(ev) {
		const item = ev.target.getParentByCssClass("item");
		if (item) {
			this._events.fire("image-selected", item.getUserData());
		}
	}

	toString() {
		return "com.kidscademy.atlas.ImagesControl";
	}
};
