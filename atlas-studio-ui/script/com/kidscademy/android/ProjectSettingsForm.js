$package("com.kidscademy.android");

com.kidscademy.android.ProjectSettingsForm = class extends com.kidscademy.View {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._app = null;

        this._form = this.getByTag("form");

        this.getByName("update").on("click", this._onUpdate, this);
    }

    onResume() {
        const releaseName = WinMain.url.parameters.release;
        ReleaseService.getAndroidAppForRelease(releaseName, this._onAppLoaded, this);
    }

    _onAppLoaded(app) {
        this._app = app;
        this._form.setObject(app);
    }

    _onUpdate() {
        if (this._form.isValid()) {
            ReleaseService.updateAndroidApp(this._form.getObject(this._app), this._onAppLoaded, this);
        }
    }

    toString() {
        return "com.kidscademy.android.ProjectSettingsForm";
    }
};
