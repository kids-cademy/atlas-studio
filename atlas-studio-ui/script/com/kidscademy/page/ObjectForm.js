$package("com.kidscademy.page");

com.kidscademy.page.ObjectForm = class extends com.kidscademy.Page {
	constructor() {
		super();

		this.CSS_INVALID = js.dom.Control.prototype.CSS_INVALID;

		this._sidebar.showObject();
		this._sidebar.on("preview", this._onPreview, this);
		this._sidebar.on("save", this._onSave, this);
		// this._sidebar.on("remove", this._onRemove, this);

		this._collectionId = Number(WinMain.url.parameters.collection);
		this._objectId = Number(WinMain.url.parameters.object) || 0;

		/**
		 * Flag true when a form control was changed. Used to signal user changes when leaving the page.
		 * @type {Boolean}
		 */
		this._dirty = false;
		WinMain.on("pre-unload", this._onPreUnload, this);

		this._form = this.getByClass(com.kidscademy.Form);
		this._form.on("input", ev => { this._dirty = true });

		const quickLinks = this.getByCssClass("quick-links");
		quickLinks.on("click", this._onQuickLinks, this);

		this._identityFieldset = this.getByClass(com.kidscademy.form.IdentityFieldSet);
		this._taxonomyControl = this.getByClass(com.kidscademy.form.TaxonomyControl);
		this._definitionControl = this.getByClass(com.kidscademy.form.DefinitionControl);
		this._descriptionControl = this.getByClass(com.kidscademy.form.DescriptionControl);
		this._graphicAssets = this.getByClass(com.kidscademy.form.GraphicAssets);
		this._audioAssets = this.getByClass(com.kidscademy.form.AudioAssets);
		this._factsControl = this.getByClass(com.kidscademy.form.FactsControl);
		this._featuresControl = this.getByClass(com.kidscademy.form.FeaturesControl);
		this._spreadingControl = this.getByClass(com.kidscademy.form.SpreadingControl);
		this._relatedControl = this.getByClass(com.kidscademy.form.RelatedControl);
		this._linksControl = this.getByClass(com.kidscademy.form.LinksControl);

		this._loadObject();
	}

	_loadObject() {
		if (this._objectId !== 0) {
			AtlasService.getAtlasObject(this._objectId, this._onObjectLoaded, this);
		}
		else {
			AtlasService.createAtlasObject(this._collectionId, this._onObjectLoaded, this);
		}
	}

	_onObjectLoaded(object) {
		this._dirty = false;

		this.findByCss(".quick-links li").removeCssClass(this.CSS_INVALID);
		this._form.reset();

		this._object = object;

		const _this = this;
		function start(flag, control) {
			if (flag) {
				control.onCreate(_this);
			}
			else {
				control.addCssClass("exclude");
				control.hide();
			}
		}

		start(this._object.collection.flags.audioSample, this._audioAssets);
		start(this._object.collection.featuresMeta.length, this._featuresControl);
		start(this._object.collection.flags.spreading, this._spreadingControl);

		this._identityFieldset.onCreate(this);
		this._taxonomyControl.onCreate(this);
		this._definitionControl.onCreate(this);
		this._descriptionControl.onCreate(this);
		this._graphicAssets.onCreate(this);
		this._factsControl.onCreate(this);
		this._relatedControl.onCreate(this);
		this._linksControl.onCreate(this);

		this._form.setObject(object);
		this._sidebar.setObject(object);
	}

	_onUnload() {
		this._identityFieldset.onDestroy(this);
		this._taxonomyControl.onDestroy();
		this._definitionControl.onDestroy();
		this._descriptionControl.onDestroy();
		this._graphicAssets.onDestroy();
		this._audioAssets.onDestroy();
		this._factsControl.onDestroy();
		this._featuresControl.onDestroy();
		this._spreadingControl.onDestroy();
		this._relatedControl.onDestroy();
		this._linksControl.onDestroy();
	}

	getObject() {
		return this._object;
	}

	getCollection() {
		return this._object.collection;
	}

	getAtlasItem() {
		const atlasItem = {
			id: this._object.id,
			collection: this._object.collection,
			name: this._object.name
		}
		return atlasItem;
	}

	getLinks(feature) {
		const object = this.getObject();
		if (!object.links) {
			return [];
		}
		return object.links.filter(link => link.features.indexOf(feature) !== -1);
	}

	_getDefinitionControl() {
		return this._definitionControl;
	}

	_getDescriptionControl() {
		return this._descriptionControl;
	}

	// --------------------------------------------------------------------------------------------

	_onPreview() {
		WinMain.assign("@link/reader", { object: this._object.id });
	}

	_onSave() {
		this.findByCss(".quick-links li").removeCssClass(this.CSS_INVALID);
		const updateQuickLink = control => {
			const fieldset = control.getParentByTag("fieldset");
			// by convention quick link class is the fieldset ID
			this.getByCss(`.quick-links [data-name=${fieldset.getAttr("id")}]`).addCssClass(this.CSS_INVALID);
		};

		const object = this._form.getObject(this._object);
		const reload = object.id === 0;
		if (this._form.isValid(updateQuickLink)) {
			AtlasService.saveAtlasObject(object, object => {
				if (reload) {
					WinMain.assign("@link/object-form", { object: object.id });
					return;
				}
				this._onObjectLoaded(object);
			});
		}
	}

	_onQuickLinks(ev) {
		const quickLink = ev.target.getParentByTag("li");
		if (quickLink != null) {
			// by convention quick link name is fieldset ID
			const fieldsetID = quickLink.getAttr("data-name");
			if (fieldsetID != null) {
				this.getById(fieldsetID).scrollIntoView();
				return;
			}
		}
	}

	_onBack() {
		if (this._dirty) {
			js.ua.System.confirm("@string/confirm-object-not-saved", ok => {
				if (ok) {
					WinMain.assign("collection.htm");
				}
			});
			return;
		}
		super._onBack();
	}

	_onPreUnload(ev) {
		if (this._dirty) {
			return "@string/confirm-object-not-saved";
		}
	}

	toString() {
		return "com.kidscademy.page.ObjectForm";
	}
};

WinMain.createPage(com.kidscademy.page.ObjectForm);
