$package("com.kidscademy.atlas");

/**
 * Control for related objects. It has two sections: currently selected related objects and a collection with
 * available, not yet selected, objects. Moving from one section to another is performed via drag and drop.
 */
com.kidscademy.atlas.RelatedControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Parent form page.
		 * @type {com.kidscademy.atlas.FormPage}
		 */
		this._formPage = null;

		this._relatedView = this.getByCss(".list-view.related");
		this._relatedView.on("dragstart", this._onDragStart, this);
		this._relatedView.on("dragover", this._onDragOver, this);
		this._relatedView.on("drop", this._onRelatedViewDrop, this);

		this._collectionView = this.getByCss(".list-view.collection");
		this._collectionView.on("dragstart", this._onDragStart, this);
		this._collectionView.on("dragover", this._onDragOver, this);
		this._collectionView.on("drop", this._onCollectionViewDrop, this);

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this).hide("close");
	}

	onCreate(formPage) {
		this._formPage = formPage;
	}

	onStart() {
	}

	// --------------------------------------------------------------------------------------------
	// CONTROL INTERFACE

	setValue(related) {
		const names = related.map(object => object.name);
		AtlasService.getRelatedInstruments(names, objects => {
			objects.forEach(object => object.href = `@link/admin-object?id=${object.id}`);
			this._relatedView.setObject(objects)
		});
	}

	getValue() {
		const related = [];
		this._relatedView.getChildren().forEach(instrumentView => {
			// template item has no id attribute
			if (instrumentView.getAttr("id") !== null) {
				related.push({ name: instrumentView.getUserData("value").name });
			}
		});
		return related;
	}

	isValid() {
		return true;
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onEdit() {
		this._actions.show("close");
		const instrument = this._formPage.getObject();
		if (!instrument.category) {
			js.ua.System.alert("Pleae select category.");
			return;
		}

		const instruments = [];
		this._relatedView.getChildren().forEach(view => instruments.push({ id: view.getAttr("id") }));

		instruments.push({
			id: instrument.id
		});

		AtlasService.getAvailableInstruments(instrument.category, instruments, collection => {
			this._collectionView.show();
			this._collectionView.setObject(collection);
		});
	}

	_onClose() {
		this._collectionView.hide();
		this._actions.hide("close");
	}

	// --------------------------------------------------------------------------------------------
	// DRAG AND DROP
	
	_onDragStart(ev) {
		const li = ev.target.getParentByTag("li");
		ev.setData("index", li.getChildIndex());
	}

	_onDragOver(ev) {
		ev.prevent();
	}

	_onRelatedViewDrop(ev) {
		ev.prevent();
		this._relatedView.addChild(this._collectionView.getByIndex(ev.getData("index")));
	}

	_onCollectionViewDrop(ev) {
		ev.prevent();
		this._collectionView.addChild(this._relatedView.getByIndex(ev.getData("index")));
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.RelatedControl";
	}
};
