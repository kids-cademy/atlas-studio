$package("com.kidscademy.page");

com.kidscademy.page.AppsPage = class extends js.ua.Page {
    constructor() {
        super();
        this._appsListView = this.getByCssClass("icons-list");
        ReleaseService.getApplications(this._onAppsLoaded, this);
    }

    _onAppsLoaded(apps) {
        apps.forEach(app => {
            app.href = `export-android-apk.xsp?name=${app.name}`;
            app.download = `${app.name}.apk`;
        });
        this._appsListView.setObject(apps).show();
    }

    toString() {
        return "com.kidscademy.page.AppsPage";
    }
};

WinMain.createPage(com.kidscademy.page.AppsPage);
