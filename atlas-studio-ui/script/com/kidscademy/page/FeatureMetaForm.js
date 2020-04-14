$package("com.kidscademy.page");

com.kidscademy.page.FeatureMetaForm = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._feature = null;
        this._form = this.getByTag("form");

        const featureId = Number(WinMain.url.parameters.feature);
        if (featureId) {
            AtlasService.getFeatureMeta(featureId, this._onFeatureLoaded, this);
        }
        else {
            AtlasService.createFeatureMeta(this._onFeatureLoaded, this);
        }

        this.getByName("save").on("click", this._onSave, this);
        this.getByName("cancel").on("click", this._onCancel, this);
    }

    _onFeatureLoaded(feature) {
        this._feature = feature;
        this._form.setObject(feature);
    }

    _onSave(ev) {
        if (this._form.isValid()) {
            AtlasService.saveFeatureMeta(this._form.getObject(this._feature), () => WinMain.back());
        }
    }

    _onCancel(ev) {
        WinMain.back();
    }

    toString() {
        return "com.kidscademy.page.FeatureMetaForm";
    }
};

WinMain.createPage(com.kidscademy.page.FeatureMetaForm);
