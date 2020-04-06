$package("com.kidscademy.android");

com.kidscademy.android.StoreListingView = class extends com.kidscademy.View {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);
        this._form = this.getByTag("form");
        this._form.on("click", this._onClick, this);
    }

    onResume() {
        ReleaseService.getAndroidStoreListing(this._page._app.id, listing => {
            this._form.setObject(listing);
        });
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
        return "com.kidscademy.android.StoreListingView";
    }
};
