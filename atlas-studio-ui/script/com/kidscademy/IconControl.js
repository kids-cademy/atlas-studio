$package("com.kidscademy.workspace");

com.kidscademy.IconControl = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._object = { id: 0 };
        this._imageKind = null;
        this._image = {};

        this._imageEditor = this.getByClass(com.kidscademy.ImageEditor);
        this._imageEditor.config({
            aspectRatio: 1
        });

        this._imageEditor.on("open", this._onImageEditorOpen, this);
        this._imageEditor.on("close", this._onImageEditorClose, this);
        this._imageEditor.on("upload", this._onImageEditorUpload, this);
        this._imageEditor.on("link", this._onImageEditorLink, this);
        this._imageEditor.on("change", this._onImageEditorChange, this);
        this._imageEditor.on("remove", this._onImageEditorRemove, this);

        this._metaForm = this.getByClass(com.kidscademy.FormData);

        this._imageView = this.getByClass(js.dom.ImageControl);
        this._imageView.on("click", this._onImageClick, this);
    }

    config(config) {
        this._object = config.object;
        this._imageKind = config.imageKind;
    }

    _onImageClick(ev) {
        this._metaForm.open();
        this._image.src = ev.target.getAttr("src");
        this._imageEditor.open(this._image, image => {
            this._imageView.setValue(image.src);
        });
    }

    _onImageEditorOpen() {
        this._metaForm.open();
    }

    _onImageEditorClose(image) {
        this._metaForm.hide();
        this._imageView.setValue(image.src);
    }

    _onImageEditorUpload(handler) {
        const formData = this._metaForm.getFormData();
        formData.append("image-kind", this._imageKind);
        formData.append("object-id", this._object.id);
        formData.append("media-file", handler.file);

        AtlasService.uploadImage(formData, image => {
            this._image = image;
            this._imageView.setValue(image.src);
            handler.callback(image);
        });
    }

    _onImageEditorLink(callback) {
        if (!this._metaForm.isValid()) {
            return;
        }

        const formData = this._metaForm.getFormData();
        formData.append("image-kind", this._imageKind);
        formData.append("object-id", this._object.id);

        AtlasService.uploadImageBySource(formData, image => {
            this._image = image;
            this._imageView.setValue(image.src);
            callback(image);
        });
    }

    _onImageEditorChange(image) {
        this._metaForm.setObject(image);
    }

    _onImageEditorRemove(image) {
        this._imageView.remove();
    }

    toString() {
        return "com.kidscademy.IconControl";
    }
};
