$package("com.kidscademy.workspace");

com.kidscademy.workspace.CollectionForm = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._collection = null;

        this._form = this.getByTag("form");

        this._iconSection = this.getByCssClass("icon-section");

        this._iconControl = this.getByClass(com.kidscademy.workspace.IconControl);
        this._taxonomyMetaControl = this.getByClass(com.kidscademy.workspace.TaxonomyMetaControl);

        this.getByCss(".buttons-bar [name='save']").on("click", this._onSave, this);
        this.getByCss(".buttons-bar [name='cancel']").on("click", this._onCancel, this);
    }

    open(collection, callback) {
        this._collection = collection;
        this._callback = callback;
        if (collection.id === 0) {
            this._iconSection.hide().addCssClass("exclude");
        }
        else {
            this._iconControl.setCollection(collection);
        }
        this._form.setObject(collection);
    }

    _onSave(ev) {
        if (this._form.isValid()) {
            this._form.getObject(this._collection);
            this._callback(this._collection);
        }
    }

    _onCancel(ev) {
        this._callback(null);
    }

    toString() {
        return "com.kidscademy.workspace.CollectionForm";
    }
};
