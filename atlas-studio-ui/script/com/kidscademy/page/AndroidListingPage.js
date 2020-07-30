$package("com.kidscademy.page");

com.kidscademy.page.AndroidListingPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("Android Store Listing");
        this._sidebar.on("edit-release", this._onEditRelease, this);
        this._sidebar.on("android-app", this._onAndroidApp, this);
        this._sidebar.getByName("download-apk").on("click", this._onDownloadAPK, this);
        this._sidebar.getByName("download-bundle").on("click", this._onDownloadBundle, this);

        this._form = this.getByTag("form");
        this._form.on("click", this._onClick, this);

        this._appId = Number(WinMain.url.parameters.app);
        ReleaseService.getAndroidStoreListing(this._appId, listing => {
            this._listing = listing;
            this._form.setObject(listing);
        });
    }

    _onEditRelease() {
        WinMain.assign("@link/release-form", { release: this._listing.releaseId });
    }

    _onAndroidApp() {
        WinMain.assign("@link/android-app", { app: this._appId });
    }

    _onClick(ev) {
        function attachedControl(action) {
            return action.getParent().getPreviousSibling();
        }

        switch (ev.target.getName()) {
            case "copy":
                Sound.beep();
                const control = attachedControl(ev.target);
                control.removeAttr("disabled");
                control.copyToClipboard();
                control.setAttr("disabled", "disabled");
                break;

            case "browse":
                window.open(attachedControl(ev.target).getValue());
                break;
        }
    }

    _onDownloadAPK(ev) {
        const anchor = ev.target.getParentByTag("a");
        anchor.setAttr("download", `${this._listing.name}.apk`);
        anchor.setAttr("href", `export-android-apk.xsp?name=${this._listing.name}`);
    }

    _onDownloadBundle(ev) {
        const anchor = ev.target.getParentByTag("a");
        anchor.setAttr("download", `${this._listing.name}.aab`);
        anchor.setAttr("href", `export-android-bundle.xsp?name=${this._listing.name}`);
    }

    toString() {
        return "com.kidscademy.page.AndroidListingPage";
    }
};

WinMain.createPage(com.kidscademy.page.AndroidListingPage);
