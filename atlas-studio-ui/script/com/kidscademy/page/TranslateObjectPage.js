$package("com.kidscademy.page");

com.kidscademy.page.TranslateObjectPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.on("identity", this._onIdentity, this);
        this._sidebar.on("description", this._onDescription, this);
        this._sidebar.on("media", this._onMedia, this);
        this._sidebar.on("facts", this._onFacts, this);

        this._sidebar.on("preview", this._onPreview, this);
        this._sidebar.on("save", this._onSave, this);
        this._sidebar.on("translate", this._onTranslate, this);
        this._sidebar.on("remove", this._onRemove, this);

        this._form = this.getByTag("form");

        this._objectId = Number(WinMain.url.parameters.object);
        this._language = WinMain.url.parameters.language;
        AtlasService.getAtlasObjectTranslate(this._objectId, this._language, this._onTranslateLoaded, this);
    }

    _onTranslateLoaded(translate) {
        this._form.setObject(translate);
    }

    _onIdentity() {

    }

    _onDescription() {

    }

    _onMedia() {

    }

    _onFacts() {

    }

    _onPreview() {
        WinMain.assign("@link/reader", { object: this._objectId, language: this._language });
    }

    _onSave() {
        AtlasService.saveAtlasObjectTranslate(this._objectId, this._language, this._form.getObject());
    }

    _onTranslate() {
        AtlasService.translateAtlasObject(this._objectId, this._language, this._onTranslateLoaded, this);
    }

    _onRemove() {

    }

    toString() {
        return "com.kidscademy.page.TranslateObjectPage";
    }
};

WinMain.createPage(com.kidscademy.page.TranslateObjectPage);
