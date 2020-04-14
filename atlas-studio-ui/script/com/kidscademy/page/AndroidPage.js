$package("com.kidscademy.page");

/**
 * AndroidPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.AndroidPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of AndroidPage class.
	 */
	constructor() {
		super();

		this._app = null;

		this._objectBox = this.getByCssClass("object-box");

		const releaseName = WinMain.url.parameters.release;
		ReleaseService.getAndroidAppForRelease(releaseName, this._onAppLoaded, this);

		this._sidebar.on("clean-project", this._onCleanProject, this);
		this._sidebar.on("build-apk", this._onBuildApk, this);
		this._sidebar.on("edit-release", this._onEditRelease, this);
		this._sidebar.on("remove-app", this._onRemoveApp, this);
	}

	_onAppLoaded(app) {
		this._app = app;
		this._objectBox.setObject(app);
	}

	// --------------------------------------------------------------------------------------------
	// SIDE BAR MENU HANDLERS

	_onCleanProject() {
		ReleaseService.cleanAndroidProject(this._app.id, () => js.ua.System.alert("@string/alert-processing-done"));
	}

	_onBuildApk() {
		ReleaseService.buildAndroidApp(this._app.id, () => js.ua.System.alert("@string/alert-processing-done"));
	}

	_onEditRelease() {
		WinMain.assign("@link/release", { release: this._app.release.name });
	}

	_onRemoveApp() {
		js.ua.System.confirm("@string/confirm-app-remove", ok => {
			if (ok) {
				ReleaseService.removeAndroidApp(this._app.id, () => WinMain.assign("@link/release"));
			}
		});
	}

	toString() {
		return "com.kidscademy.page.AndroidPage";
	}
};

WinMain.createPage(com.kidscademy.page.AndroidPage);
