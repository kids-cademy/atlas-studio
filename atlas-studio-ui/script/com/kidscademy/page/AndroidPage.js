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

		this._form = this.getByCssClass("form");
		this._objectBox = this.getByCssClass("object-box");

		const releaseName = WinMain.url.parameters.release;
		ReleaseService.getAndroidAppForRelease(releaseName, this._onAppLoaded, this);

		this.getByName("update").on("click", this._onUpdate, this);

		const menu = this.getByCss(".side-bar .menu");
		menu.on(this, {
			"&clean-project": this._onCleanProject,
			"&build-apk": this._onBuildApk
		});

		const actions = this.getByCss(".side-bar .menu.bottom");
		actions.on(this, {
			"&edit-release": this._onEditRelease,
			"&remove-app": this._onRemoveApp
		});

		this.on("clean-project", this._onCleanProject, this);
	}

	_onAppLoaded(app) {
		this._app = app;
		this._form.setObject(app);
		this._objectBox.setObject(app);
	}

	_onUpdate() {
		if (this._form.isValid()) {
			ReleaseService.updateAndroidApp(this._form.getObject(this._app), this._onAppLoaded, this);
		}
	}

	_onCleanProject() {
		ReleaseService.cleanAndroidProject(this._form.getObject(this._app));
	}

	_onBuildApk() {
		ReleaseService.buildAndroidApp(this._form.getObject(this._app));
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
