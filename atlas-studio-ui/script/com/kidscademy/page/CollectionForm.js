$package("com.kidscademy.page");

com.kidscademy.page.CollectionForm = class extends com.kidscademy.Page {
    constructor() {
        super();
        
		this.CSS_INVALID = js.dom.Control.prototype.CSS_INVALID;

        this._sidebar.setTitle("@string/collections");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("external-sources", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);

        this._sidebar.on("save", this._onSave, this);
        this._sidebar.on("cancel", this._onCancel, this);

        this._form = this.getByTag("form");
        this._iconSection = this.getById("icon");
        this._iconControl = this._form.getByClass(com.kidscademy.IconControl);

        this._apisSelect = this._form.getByClass(com.kidscademy.ApisSelect);

        const quickLinks = this.getByCssClass("quick-links");
        quickLinks.on("click", this._onQuickLinks, this);
        
        const collectionId = Number(WinMain.url.parameters.collection);
        if (collectionId) {
            AtlasService.getCollection(collectionId, this._onCollectionLoaded, this);
        }
        else {
            AtlasService.createAtlasCollection(this._onCollectionLoaded, this);
        }
    }

    getCollectionId() {
        return this._collection.id;
    }

    _onCollectionLoaded(collection) {
        this._collection = collection;
        if (collection.id === 0) {
            this._iconSection.hide().addCssClass("exclude");
        }
        else {
            this._iconSection.show().removeCssClass("exclude");
            this._iconControl.config({
                object: collection,
                imageKind: "COLLECTION"
            });
        }
        this._sidebar.setObject(collection);
        this._form.setObject(collection);
    }

    _onSave() {
        this.findByCss(".quick-links li").removeCssClass(this.CSS_INVALID);
		const updateQuickLink = control => {
			const fieldset = control.getParentByTag("fieldset");
			// by convention quick link class is the fieldset ID
			this.getByCss(`.quick-links [data-name=${fieldset.getAttr("id")}]`).addCssClass(this.CSS_INVALID);
		};

        if (this._form.isValid(updateQuickLink)) {
            AtlasService.saveAtlasCollection(this._form.getObject(this._collection), () => WinMain.assign("@link/collections"));
        }
    }

    _onCancel() {
        WinMain.back();
    }

    _onCollections() {
        WinMain.assign("@link/collections");
    }

    _onLinksMeta() {
        WinMain.assign("@link/external-sources");
    }

    _onFeaturesMeta() {
        WinMain.assign("@link/features-meta");
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

    toString() {
        return "com.kidscademy.page.CollectionForm";
    }
};

WinMain.createPage(com.kidscademy.page.CollectionForm);
