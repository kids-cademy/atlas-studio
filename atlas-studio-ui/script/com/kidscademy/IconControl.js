$package("com.kidscademy.workspace");

com.kidscademy.IconControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._object = { id: 0 };
        this._imageKind = null;
        this._image = {};

        this._imageEditor = this.getByClass(com.kidscademy.ImageEditor);
        this._imageEditor.config({ aspectRatio: 1 });
		this._imageEditor.on("image-paste", this._onImagePaste, this);

        this._metaForm = this.getByClass(com.kidscademy.FormData);

        this._imageView = this.getByClass(js.dom.ImageControl);
        this._imageView.on("click", this._onImageClick, this);

        this._actions = this._imageEditor.getActions().bind(this);
        this._actions.disable("image-remove");
        this._actions.showOnly("add");
        // register event for hidden input of type file to trigger image loading from host OS
        this._actions.getByName("file-upload").on("change", this._onUploadFile, this);
    }

    config(config) {
        this._object = config.object;
        this._imageKind = config.imageKind;
    }

    // --------------------------------------------------------------------------------------------
    // CONTROL INTERFACE

    setValue(iconSrc) {
        this._imageView.setValue(iconSrc);
    }

    getValue() {
        return this._imageView.getValue();
    }

    isValid() {
        return this._imageView.isValid();
    }

    // --------------------------------------------------------------------------------------------
    // ACTION HANDLERS

    _onAdd() {
        this._actions.showOnly("image-upload", "image-link", "close");
        this._imageEditor.open(this._onEditorDone.bind(this));
        this._metaForm.open();
    }

    _onImageUpload(ev) {
        if (!this._metaForm.isValid()) {
            // stop bubbling and default behavior for click event
            // by doing this, 'change' event for inner file input is not longer fired
            // and next _onUploadFile handler is not invoked
            ev.halt();
            return;
        }

        // here object name exists and meta form is valid, so we can upload media file
        // after this click handler exit, 'change' event for inner file input is fired
        // and next _onUploadFile is executed

        this._metaForm.hide();
    }

    _onUploadFile(ev) {
        $assert(this._imageKind != null, "com.kidscademy.IconControl#onUploadFile", "Null parent object class.");
        $assert(this._object != null, "com.kidscademy.IconControl#onUploadFile", "Null parent object.");

        const formData = this._metaForm.getFormData();
        formData.append("image-key", "ICON");
        formData.append("image-kind", this._imageKind);
        formData.append("object-id", this._object.id);
        formData.append("media-file", ev.target._node.files[0]);

        AtlasService.uploadImage(formData, image => this._imageEditor.open(this._onEditorDone.bind(this), image));
    }

    _onImageLink(ev) {
        $assert(this._imageKind != null, "com.kidscademy.IconControl#onImageLink", "Null parent object class.");
        $assert(this._object != null, "com.kidscademy.IconControl#onImageLink", "Null parent object.");

        if (!this._metaForm.isValid()) {
            ev.halt();
            return;
        }
        const formData = this._metaForm.getFormData();
        formData.append("image-key", "ICON");
        formData.append("image-kind", this._imageKind);
        formData.append("object-id", this._object.id);

        AtlasService.uploadImageBySource(formData, image => this._imageEditor.open(this._onEditorDone.bind(this), image));
    }

	_onImagePaste(imageFile) {
        $assert(this._imageKind != null, "com.kidscademy.IconControl#onUploadFile", "Null parent object class.");
        $assert(this._object != null, "com.kidscademy.IconControl#onUploadFile", "Null parent object.");
		if (!this._metaForm.isValid()) {
			return;
		}

		const formData = this._metaForm.getFormData();
		formData.append("image-key", "ICON");
        formData.append("image-kind", this._imageKind);
        formData.append("object-id", this._object.id);
		formData.append("media-file", imageFile);

        AtlasService.uploadImage(formData, image => this._imageEditor.open(this._onEditorDone.bind(this), image));
	}

    _onMetaForm() {
        this._metaForm.show(!this._metaForm.isVisible());
    }

    // --------------------------------------------------------------------------------------------

    _onImageClick(ev) {
        this._metaForm.open();
        this._image.src = ev.target.getValue();
        this._imageEditor.open(this._onEditorDone.bind(this), this._image);
    }

    _onEditorDone(image) {
        this._actions.showOnly("add");
        this._metaForm.hide();
        if (image != null) {
            this._imageView.setValue(image.src);
        }
    }

    toString() {
        return "com.kidscademy.IconControl";
    }
};
