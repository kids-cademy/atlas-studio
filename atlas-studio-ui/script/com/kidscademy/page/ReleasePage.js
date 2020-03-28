$package("com.kidscademy.page");

/**
 * Atlas release manager.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.ReleasePage = class extends com.kidscademy.page.Page {
    constructor() {
        super();

        this._releaseInfoBox = this.getByCssClass("release-info");

        this._contentView = this.getByClass(com.kidscademy.FrameView);
        this._contentView.select("releases-list");

        this._releasesList = this.getByClass(com.kidscademy.release.ReleasesList);
        this._objectsList = this.getByClass(com.kidscademy.release.ObjectsList);

        this._releasesList.onCreate(this);
        this._objectsList.onCreate(this);
    }

    selectView(viewName, arg) {
        this._contentView.select(viewName);
    }

    _onUnload() {
        this._releasesList.onDestroy(this);
        this._objectsList.onDestroy(this);
    }

    toString() {
        return "com.kidscademy.page.ReleasePage";
    }
};

WinMain.createPage(com.kidscademy.page.ReleasePage);
