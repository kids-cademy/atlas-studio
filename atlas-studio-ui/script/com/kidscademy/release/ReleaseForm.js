$package("com.kidscademy.release");

com.kidscademy.release.ReleaseForm = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._release = null;

        this._form = this.getByTag("form");
        this._nameInput = this._form.getByName("name");
        this._packageNameInput = this._form.getByName("package-name");
        this._propertiesSection = this._form.getByCssClass("properties-section");

        this._graphicAssets = this._form.getByClass(com.kidscademy.release.GraphicAssets);

        this.getByName("save").on("click", this._onSave, this);
        this.getByName("cancel").on("click", this._onCancel, this);
    }

    open(release, callback) {
        this._release = release;
        this._callback = callback;

        if (!release.properties) {
            this._nameInput.removeAttr("disabled");
            this._packageNameInput.removeAttr("disabled");
            this._propertiesSection.hide().addCssClass("exclude");
        }
        else {
            this._nameInput.setAttr("disabled", "disabled");
            this._packageNameInput.setAttr("disabled", "disabled");
            this._propertiesSection.show().removeCssClass("exclude");
        }

        this._form.setObject(release);
    }

    _onSave(ev) {
        if (this._form.isValid()) {
            this._form.getObject(this._release);
            this._callback(this._release);
        }
    }

    _onCancel(ev) {
        this._callback(null);
    }

    toString() {
        return "com.kidscademy.release.ReleaseForm";
    }
};
