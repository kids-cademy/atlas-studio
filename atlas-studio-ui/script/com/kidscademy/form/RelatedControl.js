$package("com.kidscademy.form");

/**
 * Control for related objects. It has two sections: currently selected related objects and a collection with
 * available, not yet selected, objects. Moving from one section to another is performed via drag and drop.
 */
com.kidscademy.form.RelatedControl = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._relatedView = this.getByCss(".list-view.related");
		this._relatedView.on("dragstart", this._onDragStart, this);
		this._relatedView.on("dragover", this._onDragOver, this);
		this._relatedView.on("drop", this._onRelatedViewDrop, this);

		this._candidatesView = this.getByCss(".list-view.candidates");
		this._candidatesView.on("dragstart", this._onDragStart, this);
		this._candidatesView.on("dragover", this._onDragOver, this);
		this._candidatesView.on("drop", this._onCandidatesViewDrop, this);

		this._taxonomySelect = this.getByCssClass("taxonomy-select");
		this._taxonomySelect.on("change", this._onTaxonomySelectChange, this);

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this).showOnly("search");
	}

	onStart() {
		const taxonomy = this._formPage.getObject().taxonomy;
		this._taxonomySelect.setOptions(taxonomy);
	}

	// --------------------------------------------------------------------------------------------
	// CONTROL INTERFACE

	setValue(names) {
		const collectionId = this._formPage.getCollection().id;
		AtlasService.getRelatedAtlasObjects(collectionId, names, objects => {
			objects.forEach(object => object.href = `@link/form?id=${object.id}`);
			this._relatedView.setObject(objects);
		});
	}

	getValue() {
		const related = [];
		this._relatedView.getChildren().forEach(objectView => {
			// template item has no id attribute
			if (objectView.getAttr("id") !== null) {
				related.push(objectView.getUserData("value").name);
			}
		});
		return related;
	}

	isValid() {
		return true;
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onSearch() {
		this._actions.show("close");
		const object = this._formPage.getObject();

		var taxon = null;
		switch (object.taxonomy.length) {
			case 0:
				js.ua.System.alert("Please set atlas object taxonomy. It is needed to filter out related objects.");
				return;

			case 1:
				this._getCollectionItemsByTaxon(object.taxonomy[0]);
				break;

			default:
				this._candidatesView.hide();
				this._taxonomySelect.show();
				this._taxonomySelect.reset();
			// _getCollectionItemsByTaxon(taxon) is invoked when user make taxon selection, see _onTaxonomySelectChange(ev)
		}
	}

	_onClose() {
		this._taxonomySelect.hide();
		this._candidatesView.hide();
		this._actions.hide("close");
	}

	_onTaxonomySelectChange(taxon) {
		this._getCollectionItemsByTaxon(taxon);
		this._taxonomySelect.hide();
	}

	_getCollectionItemsByTaxon(taxon) {
		const excludes = [];
		this._relatedView.getChildren().forEach(view => excludes.push({ id: view.getAttr("id") }));
		excludes.push({
			id: this._formPage.getObject().id
		});

		const collectionId = this._formPage.getCollection().id;
		AtlasService.getCollectionItemsByTaxon(collectionId, taxon, excludes, items => {
			this._candidatesView.show();
			this._candidatesView.setObject(items);
		});
	}

	// --------------------------------------------------------------------------------------------
	// DRAG AND DROP

	_onDragStart(ev) {
		const li = ev.target.getParentByTag("li");
		ev.setData({
			source: li.getParent().getAttr("data-source"),
			index: li.getChildIndex()
		});
	}

	_onDragOver(ev) {
		ev.prevent();
	}

	_onRelatedViewDrop(ev) {
		ev.prevent();
		const data = ev.getData();
		if (data.source === "related") {
			// drag from the same related objects view for reordering
			const sourceElement = this._relatedView.getByIndex(data.index);
			const targetElement = ev.target.getParentByTag("li");
			if (targetElement != null && targetElement !== sourceElement) {
				if (ev.ctrlKey) {
					const siblingElement = targetElement.getNextSibling();
					if (siblingElement == null) {
						targetElement.getParent().addChild(sourceElement);
					}
					else {
						siblingElement.insertBefore(sourceElement);
					}
				}
				else {
					targetElement.insertBefore(sourceElement);
				}
			}
			return;
		}
		this._relatedView.addChild(this._candidatesView.getByIndex(data.index));
		this._fireEvent("input");
	}

	_onCandidatesViewDrop(ev) {
		ev.prevent();
		const data = ev.getData();
		if (data.source === "candidates") {
			return;
		}
		this._candidatesView.addChild(this._relatedView.getByIndex(data.index));
		this._fireEvent("input");
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.RelatedControl";
	}
};
