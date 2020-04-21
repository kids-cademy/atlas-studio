$package("com.kidscademy.page");

com.kidscademy.page.ReleaseForm = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.showObject();
        this._sidebar.on("release-objects", this._onReleaseObjects, this);
        this._sidebar.on("android-app", this._onAndroidApp, this);

        this._release = null;
        this._form = this.getByTag("form");

        /**
         * Form fieldsets displayed only on release edit, that is, not displayed when release is newly created. All
         * controls from these fieldssets have sensible default / initial values.
         * @type {js.dom.Element}
         */
        this._editSections = this.getByCssClass("edit-sections");

        const releaseId = Number(WinMain.url.parameters.release);
        if (releaseId) {
            ReleaseService.getRelease(releaseId, this._onReleaseLoaded, this);
        }
        else {
            ReleaseService.createRelease(this._onReleaseLoaded, this);
        }

        this.getByName("save").on("click", this._onSave, this);
        this.getByName("cancel").on("click", this._onCancel, this);
    }

    getRelease() {
        return this._release;
    }

    _onReleaseLoaded(release) {
        this._release = release;
        this._editSections.addCssClass("exclude", release.id === 0);
        this._editSections.addCssClass("hidden", release.id === 0);

        this._sidebar.setObject(release);
        this._form.setObject(release);
    }

    _onReleaseObjects() {
        WinMain.assign("@link/release", { release: this._release.id });
    }

    _onAndroidApp() {
        WinMain.assign("@link/android-app", { release: this._release.name });
    }

    _onSave(ev) {
        if (this._form.isValid()) {
            ReleaseService.saveRelease(this._form.getObject(this._release), () => WinMain.back());
        }
    }

    _onCancel(ev) {
        WinMain.back();
    }

    toString() {
        return "com.kidscademy.page.ReleaseForm";
    }
};

WinMain.createPage(com.kidscademy.page.ReleaseForm);
