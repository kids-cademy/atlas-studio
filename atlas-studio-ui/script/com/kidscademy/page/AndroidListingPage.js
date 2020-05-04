$package("com.kidscademy.page");

com.kidscademy.page.AndroidListingPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("Android Store Listing");
        this._sidebar.on("edit-release", this._onEditRelease, this);
        this._sidebar.on("android-app", this._onAndroidApp, this);

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
        if (ev.target.hasCssClass("action-for-input")) {
            Sound.beep();
            const control = ev.target.getPreviousSibling();
            control.removeAttr("disabled");
            control.copyToClipboard();
            control.setAttr("disabled", "disabled");
        }
    }

    toString() {
        return "com.kidscademy.page.AndroidListingPage";
    }
};

WinMain.createPage(com.kidscademy.page.AndroidListingPage);
