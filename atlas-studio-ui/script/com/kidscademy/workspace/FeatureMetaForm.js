$package("com.kidscademy.workspace");

com.kidscademy.workspace.FeatureMetaForm = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._feature = null;
        this._form = this.getByTag("form");

        this.getByName("save").on("click", this._onSave, this);
        this.getByName("cancel").on("click", this._onCancel, this);
    }

    open(feature, callback) {
        this._feature = feature;
        this._callback = callback;
        this._form.setObject(feature);
    }

    _onSave(ev) {
        // assert this._feature != null
        if (this._form.isValid()) {
            this._form.getObject(this._feature);
            this._callback(this._feature);
        }
    }

    _onCancel(ev) {
        this._callback(null);
    }

    toString() {
        return "com.kidscademy.workspace.FeatureMetaForm";
    }
};
