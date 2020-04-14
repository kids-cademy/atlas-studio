$package("com.kidscademy.page");

com.kidscademy.page.AndroidSettingsPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("Project Settings");
        this._sidebar.on("android-app", this._onAndroidApp, this);

        this._form = this.getByTag("form");
        this._appId = Number(WinMain.url.parameters.app);
        ReleaseService.getAndroidApp(this._appId, this._onAppLoaded, this);

        this.getByName("update").on("click", this._onUpdate, this);
    }

    _onAndroidApp() {
        WinMain.assign("@link/android-app", { app: this._appId });
    }

    _onAppLoaded(app) {
        this._app = app;
        this._form.setObject(app);
    }

    _onUpdate() {
        if (this._form.isValid()) {
            ReleaseService.updateAndroidApp(this._form.getObject(this._app), () => WinMain.back());
        }
    }

    toString() {
        return "com.kidscademy.page.AndroidSettingsPage";
    }
};

WinMain.createPage(com.kidscademy.page.AndroidSettingsPage);
