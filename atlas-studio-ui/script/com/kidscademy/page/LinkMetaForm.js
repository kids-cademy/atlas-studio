$package("com.kidscademy.page");

com.kidscademy.page.LinkMetaForm = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/link-meta");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("links-meta", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);

        this._linkMeta = null;
        this._form = this.getByTag("form");

        this._domainControl = this._form.getByName("domain");
        this._domainControl.on("paste", this._onDomainPaste, this);

        this._iconSection = this.getByCssClass("icon-section");
        this._iconControl = this.getByClass(com.kidscademy.IconControl);

        this._apisListView = this.getByCssClass("apis-list");

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

        const features = linkMeta.features.split(',');
        ApiService.getAvailableApis(apis => {
            this._apisListView.setObject(apis);

            this._apisListView.findByTag("tr").forEach(tr => {
                tr.getByTag("input").check(features.includes(tr.getByCssClass("name").getText()));
            });
        });
        this._form.setObject(linkMeta);
    }

    _onDomainPaste(ev) {
        ev.halt();
        const url = ev.getData();
        const domain = Strings.basedomain(url);
        this._domainControl.setValue(domain != null ? domain : url);
    }

    _onSave(ev) {
        if (this._form.isValid()) {
            var features = [];
            this._apisListView.findByTag("tr").forEach(tr => {
                if (tr.getByTag("input").checked()) {
                    features.push(tr.getByCssClass("name").getText());
                }
            });
            this._linkMeta.features = features.join();
            AtlasService.saveLinkMeta(this._form.getObject(this._linkMeta), () => WinMain.back());
        }
    }

    _onCancel(ev) {
        WinMain.back();
    }

    _onCollections() {
        WinMain.assign("@link/collections");
    }

    _onLinksMeta() {
        WinMain.assign("@link/links-meta");
    }

    _onFeaturesMeta() {
        WinMain.assign("@link/features-meta");
    }

    toString() {
        return "com.kidscademy.page.LinkMetaForm";
    }
};

WinMain.createPage(com.kidscademy.page.LinkMetaForm);
