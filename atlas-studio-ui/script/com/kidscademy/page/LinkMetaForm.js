$package("com.kidscademy.page");

com.kidscademy.page.LinkMetaform = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._linkMeta = null;
        this._form = this.getByTag("form");

        this._iconSection = this.getByCssClass("icon-section");
        this._iconControl = this.getByClass(com.kidscademy.IconControl);

        const linkMetaId = Number(WinMain.url.parameters.link);
        if (linkMetaId) {
            AtlasService.getLinkMeta(linkMetaId, this._onLinkMetaLoaded, this);
        }
        else {
            AtlasService.createLinkMeta(this._onLinkMetaLoaded, this);
        }

        this.getByName("save").on("click", this._onSave, this);
        this.getByName("cancel").on("click", this._onCancel, this);
    }

    _onLinkMetaLoaded(linkMeta) {
        this._linkMeta = linkMeta;

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
            AtlasService.saveLinkMeta(this._form.getObject(this._linkMeta), () => WinMain.back());
        }
    }

    _onCancel(ev) {
        WinMain.back();
    }

    toString() {
        return "com.kidscademy.page.LinkMetaform";
    }
};

WinMain.createPage(com.kidscademy.page.LinkMetaform);
