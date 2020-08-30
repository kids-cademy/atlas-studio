$package("com.kidscademy.form");

com.kidscademy.form.GraphicAssets = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Collection theme used to select images aspect ratio.
		 * @type {String}
		 */
		this._theme = null;

		/**
		 * Custom list control for images.
		 * @type {com.kidscademy.form.ImagesControl}
		 */
		this._imagesControl = this.getByClass(com.kidscademy.form.ImagesControl);
		this._imagesControl.on("image-selected", this._onImageSelected, this);

		/**
		 * Form data that holds meta about image, like name, source and caption.
		 * @type {com.kidscademy.FormData}
		 */
		this._metaForm = this.getByCssClass("meta");

		/**
		 * Image editor has two sections: info view - both file and crop area info, and image preview with 
		 * crop mask. It control visibility of its children.
		 * @type {com.kidscademy.ImageEditor}
		 */
		this._imageEditor = this.getByClass(com.kidscademy.ImageEditor);
		this._imageEditor.on("image-add", this._onImageAdd, this);
		this._imageEditor.on("image-upload", this._onImageUpload, this);
		this._imageEditor.on("image-save", this._onImageSave, this);
		this._imageEditor.on("image-link", this._onImageLink, this);
		this._imageEditor.on("image-meta", this._onImageMeta, this);
		this._imageEditor.on("image-remove", this._onImageRemove, this);
	}

	onCreate(formPage) {
		super.onCreate(formPage);
		this._theme = formPage.getCollection().theme;
	}

	// --------------------------------------------------------------------------------------------
	// IMAGE EDITOR EVENT HANDLERS

	_onImageAdd() {
		this._openImageEditor();
		this._metaForm.open();
		this._metaForm.enable("image-key");
		this._metaForm.enableOptions("image-key", this._imagesControl.getImageKeys());
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

	_onImageSave(imageFile) {
		if (!this._metaForm.isValid()) {
			return;
		}

		const formData = this._metaForm.getFormData();
		const object = this._formPage.getObject();

		formData.append("image-kind", "OBJECT");
		formData.append("object-id", object.id);
		formData.append("media-file", imageFile);

		const method = this._imageEditor.isVisible() ? "replaceImage" : "uploadImage";
		AtlasService[method](formData, image => this._openImageEditor(image));
	}

	_onImageLink() {
		if (!this._metaForm.mandatory("source").isValid()) {
			return;
		}
		const formData = this._metaForm.getFormData();
		const object = this._formPage.getObject();

		formData.append("image-kind", "OBJECT");
		formData.append("object-id", object.id);

		const method = this._imageEditor.isVisible() ? "replaceImageBySource" : "uploadImageBySource";
		AtlasService[method](formData, image => this._openImageEditor(image));
	}

	_onImageMeta() {
		this._metaForm.show(!this._metaForm.isVisible());
	}

	_onImageRemove() {
		js.ua.System.confirm("@string/confirm-image-remove", answer => {
			if (answer === true) {
				AtlasService.removeImage(this._formPage.getAtlasItem(), this._imageEditor.getImage(), () => {
					this._imagesControl.removeImage(this._imageEditor.getImage());
					this._imageEditor.close();
				});
			}
		});
	}

	// --------------------------------------------------------------------------------------------

	_onImageSelected(image) {
		this._metaForm.disable("image-key");
		this._metaForm.setObject(image);
		this._openImageEditor(image);
	}

	/**
	 * Display image editor on user interface for a new editing session. Image descriptor argument is
	 * used only if editing an existing image, for a new image is not provided and defaults to null.
	 * 
	 * @param {Object} image optional image descriptor, default to null.
	 */
	_openImageEditor(image = null) {
		if (image != null) {
			var aspectRatio = 0;
			switch (`${this._theme}-${image.imageKey}`) {
				case "MODERN-icon":
				case "CLASSIC-icon":
					aspectRatio = 1;
					break;

				case "MODERN-cover":
					aspectRatio = 1.5287;
					break;

				case "MODERN-featured":
					aspectRatio = 1.33;
					break;

				case "MODERN-contextual":
				case "CLASSIC-contextual":
					aspectRatio = 1.6428;
					break;
			}
			this._imageEditor.setAspectRatio(aspectRatio);
		}
		this._imageEditor.open(this._onEditorDone.bind(this), image);
	}

	_onEditorDone(image) {
		this._metaForm.hide();
		if (image != null) {
			image.imageKey = this._metaForm.getValue("image-key");
			image.source = this._metaForm.getValue("source");
			image.caption = this._metaForm.getValue("caption");

			this._imagesControl.updateImage(image);
			this._fireEvent("input");
		}
	}

	toString() {
		return "com.kidscademy.form.GraphicAssets";
	}
}
