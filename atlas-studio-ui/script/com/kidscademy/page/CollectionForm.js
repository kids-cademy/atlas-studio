$package("com.kidscademy.page");

com.kidscademy.page.CollectionForm = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/collections");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("external-sources", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);

        this._form = this.getByTag("form");
        this._iconSection = this._form.getByCssClass("icon-section");
        this._iconControl = this._form.getByClass(com.kidscademy.IconControl);

        const collectionId = Number(WinMain.url.parameters.collection);
        if (collectionId) {
            AtlasService.getCollection(collectionId, this._onCollectionLoaded, this);
        }
        else {
            AtlasService.createAtlasCollection(this._onCollectionLoaded, this);
        }

        this.getByName("save").on("click", this._onSave, this);
        this.getByName("cancel").on("click", this._onCancel, this);
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

    _onSave(ev) {
        if (this._form.isValid()) {
            AtlasService.saveAtlasCollection(this._form.getObject(this._collection), () => WinMain.back());
        }
    }

    _onCancel(ev) {
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

    toString() {
        return "com.kidscademy.page.CollectionForm";
    }
};

WinMain.createPage(com.kidscademy.page.CollectionForm);
