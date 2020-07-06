$package("com.kidscademy.page");

com.kidscademy.page.ExternalSourceForm = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/external-source");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("external-sources", this._onExternalSources, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);

        this._externalSource = null;
        this._form = this.getByTag("form");

        this._domainControl = this._form.getByName("domain");
        this._domainControl.on("paste", this._onDomainPaste, this);

        this._iconSection = this.getByCssClass("icon-section");
        this._iconControl = this.getByClass(com.kidscademy.IconControl);

        this._apisListView = this.getByCssClass("apis-list");

        const externalSourceId = Number(WinMain.url.parameters.link);
        if (externalSourceId) {
            AtlasService.getExternalSource(externalSourceId, this._onExternalSourceLoaded, this);
        }
        else {
            AtlasService.createExternalSource(this._onExternalSourceLoaded, this);
        }

        this.getByName("save").on("click", this._onSave, this);
        this.getByName("cancel").on("click", this._onCancel, this);
    }

    _onExternalSourceLoaded(externalSource) {
        this._externalSource = externalSource;

        if (externalSource.id === 0) {
            this._iconSection.hide().addCssClass("exclude");
        }
        else {
            this._iconSection.show().removeCssClass("exclude");
            this._iconControl.config({
                object: externalSource,
                imageKind: "LINK"
            });
        }

        const features = externalSource.features.split(',');
        ApiService.getAvailableApis(apis => {
            this._apisListView.setObject(apis);

            this._apisListView.findByTag("tr").forEach(tr => {
                tr.getByTag("input").check(features.includes(tr.getByCssClass("name").getText()));
            });
        });
        this._form.setObject(externalSource);
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
            this._externalSource.features = features.join();
            AtlasService.saveExternalSource(this._form.getObject(this._externalSource), () => WinMain.back());
        }
    }

    _onCancel(ev) {
        WinMain.back();
    }

    _onCollections() {
        WinMain.assign("@link/collections");
    }

    _onExternalSources() {
        WinMain.assign("@link/external-sources");
    }

    _onFeaturesMeta() {
        WinMain.assign("@link/features-meta");
    }

    toString() {
        return "com.kidscademy.page.ExternalSourceForm";
    }
};

WinMain.createPage(com.kidscademy.page.ExternalSourceForm);
