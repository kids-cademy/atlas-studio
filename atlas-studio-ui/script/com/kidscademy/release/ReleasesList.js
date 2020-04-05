$package("com.kidscademy.release");

com.kidscademy.release.ReleasesList = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        /**
         * Parent relase page.
         * @type {com.kidscademy.page.ReleasePage}
         */
        this._page = null;

        this.on("contextmenu", this._onContextMenu, this);

        this._listView = this.getByCssClass("list-view");
        this._listView.on("click", this._onListClick, this);

        this._listType = new com.kidscademy.CssFlags(this._listView, "icons", "cards");
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
        this._contextMenu = this.getByClass(com.kidscademy.ContextMenu).bind(this);
    }

    onCreate(page) {
        this._page = page;
        this._releaseForm = this._page.getByClass(com.kidscademy.release.ReleaseForm);
        this._objectsList = this._page.getByClass(com.kidscademy.release.ObjectsList);

        this._listType.set(page.getPageAttr("releases-list-type"));
        this._actions.fire("load-items");
    }

    onDestroy(page) {
        page.setPageAttr("releases-list-type", this._listType.get());
    }

    addObject(release) {
        this._listView.addObject(release);
    }

    // --------------------------------------------------------------------------------------------
    // FILTER ACTION HANDLERS

    _onLoadItems() {
        ReleaseService.getReleases(releases => this._listView.setObject(releases).show());
    }

    _onResetFilter() {

    }

    _onIconsView() {
        this._listType.set("icons");
    }

    _onCardsView() {
        this._listType.set("cards");
    }

    // --------------------------------------------------------------------------------------------
    // CONTEXT MENU HANDLERS

    _onCreateRelease() {
        ReleaseService.createRelease(release => {
            this._page.selectView("release-form");
            this._releaseForm.open(release, release => {
                this._page.selectView("releases-list");
                if (release != null) {
                    ReleaseService.saveRelease(release, release => this._listView.addObject(release));
                }
            });
        });
    }

    _onEditRelease(releaseView) {
        const release = releaseView.getUserData();
        // reload release instance in order update changes from changes
        ReleaseService.getRelease(release.id, release => {
            this._page.selectView("release-form");
            this._releaseForm.open(release, release => {
                this._page.selectView("releases-list");
                if (release != null) {
                    ReleaseService.saveRelease(release, release => releaseView.setObject(release));
                }
            });
        });
    }

    _onRemoveRelease(releaseView) {
        js.ua.System.confirm("@string/confirm-release-remove", ok => {
            if (ok) {
                const release = releaseView.getUserData();
                ReleaseService.removeRelease(release.id, () => releaseView.remove());
            }
        });
    }

    _onManageObjects(releaseView) {
        this._openObjectsList(releaseView);
    }

    _onAndroidApp(releaseView) {
        const release = releaseView.getUserData();
        WinMain.assign("@link/android", { release: release.name });
    }

    // --------------------------------------------------------------------------------------------

    _onContextMenu(ev) {
        ev.halt();
        this._contextMenu.open(ev.target.getParentByTag("li"));
    }

    _onListClick(ev) {
        const releaseView = ev.target.getParentByTag("li");
        if (releaseView != null) {
            this._openObjectsList(releaseView);
        }
    }

    _openObjectsList(releaseView) {
        this._page.selectView("objects-list");
        const release = releaseView.getUserData();
        this._objectsList.open(release);
    }

    toString() {
        return "com.kidscademy.release.ReleasesList";
    }
};
