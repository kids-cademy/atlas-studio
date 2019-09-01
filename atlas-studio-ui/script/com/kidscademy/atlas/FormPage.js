$package("com.kidscademy.atlas");

/**
 * FormPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.atlas.FormPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of FormPage class.
	 */
	constructor() {
		super();

		this.CSS_INVALID = js.dom.Control.prototype.CSS_INVALID;

		this._form = this.getByClass(com.kidscademy.Form);
		this._sidebar = this.getByCss(".side-bar .header");

		this._definitionControl = this.getByClass(com.kidscademy.atlas.DefinitionControl);
		this._descriptionControl = this.getByClass(com.kidscademy.atlas.DescriptionControl);
		this._graphicAssets = this.getByClass(com.kidscademy.atlas.GraphicAssets);
		this._audioAssets = this.getByClass(com.kidscademy.atlas.AudioAssets);
		this._factsControl = this.getByClass(com.kidscademy.atlas.FactsControl);
		this._relatedControl = this._form.getByClass(com.kidscademy.atlas.RelatedControl);
		this._linksControl = this._form.getByClass(com.kidscademy.atlas.LinksControl);

		const actions = this.getByCssClass("buttons-bar");
		actions.on(this, {
			"&save": this._onSave,
			"&reset": this._onReset,
			"&cancel": this._onCancel
		});

		this._definitionControl.onCreate(this);
		this._descriptionControl.onCreate(this);
		this._graphicAssets.onCreate(this);
		this._audioAssets.onCreate(this);
		this._factsControl.onCreate(this);
		this._relatedControl.onCreate(this);
		this._linksControl.onCreate(this);

		const quickLinks = this.getByCssClass("quick-links");
		quickLinks.on("click", this._onQuickLinks, this);

		this._publishedCheckbox = this.getByCssClass("published-object");

		this._loadObject();
	}

	getObject() {
		this._form.getObject(this._object);
		this._object.state = this._publishedCheckbox.checked() ? "PUBLISHED" : "DEVELOPMENT";
		return this._object;
	}

	getUIObject() {
		const object = this.getObject();
		return {
			id: object.id,
			dtype: object.dtype,
			name: object.name
		}
	}

	getLinks(feature) {
		const object = this.getObject();
		if (!object.links) {
			return [];
		}
		return object.links.filter(link => link.features.indexOf(feature) !== -1);
	}

	_loadObject() {
		if (typeof WinMain.url.parameters.id != "undefined") {
			var id = Number(WinMain.url.parameters.id);
			AtlasService.getInstrument(id, this._onObjectLoaded, this);
		}
		else {
			this._object = {};
		}
	}

	_onObjectLoaded(object) {
		this.findByCss(".quick-links li").removeCssClass(this.CSS_INVALID);
		this._form.reset();

		this._object = object;
		this._form.setObject(object);
		this._sidebar.setObject(object);

		this._definitionControl.onStart();
		this._descriptionControl.onStart();
		this._graphicAssets.onStart();
		this._audioAssets.onStart();
		this._factsControl.onStart();
		this._relatedControl.onStart();
		this._linksControl.onStart();
	}

	_onSave() {
		this.findByCss(".quick-links li").removeCssClass(this.CSS_INVALID);
		const updateQuickLink = control => {
			const fieldset = control.getParentByTag("fieldset");
			// by convention quick link class is the fieldset ID
			this.getByCss(`.quick-links [data-name=${fieldset.getAttr("id")}]`).addCssClass(this.CSS_INVALID);
		};

		const object = this.getObject();
		if (this._form.isValid(updateQuickLink)) {
			AtlasService.saveInstrument(object, this._onObjectLoaded, this);
		}
	}

	_onReset() {
		this._loadObject();
	}

	_onCancel() {
		WinMain.back();
	}

	_onQuickLinks(ev) {
		const quickLink = ev.target.getParentByTag("li");
		if (quickLink != null) {
			// by convention quick link name is fieldset ID
			this.getById(quickLink.getAttr("data-name")).scrollIntoView();
		}
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.FormPage";
	}
};

WinMain.createPage(com.kidscademy.atlas.FormPage);
