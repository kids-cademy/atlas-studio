$package("com.kidscademy.workspace");

com.kidscademy.workspace.LinkMetaForm = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._linkMeta = null;

        this._form = this.getByTag("form");

        this._iconSection = this.getByCssClass("icon-section");

        this._iconControl = this.getByClass(com.kidscademy.IconControl);

        this.getByName("save").on("click", this._onSave, this);
        this.getByName("cancel").on("click", this._onCancel, this);
    }

    open(linkMeta, callback) {
        this._linkMeta = linkMeta;
        this._callback = callback;
        if (linkMeta.id === 0) {
            this._iconSection.hide().addCssClass("exclude");
        }
        else {
            this._iconSection.show().removeCssClass("exclude");
            this._iconControl.config({
                object: linkMeta,
                imageKind: "LINK"
            });
        }
        this._form.setObject(linkMeta);
    }

    _onSave(ev) {
        if (this._form.isValid()) {
            this._form.getObject(this._linkMeta);
            this._callback(this._linkMeta);
        }
    }

    _onCancel(ev) {
        this._callback(null);
    }

    toString() {
        return "com.kidscademy.workspace.LinkMetaForm";
    }
};