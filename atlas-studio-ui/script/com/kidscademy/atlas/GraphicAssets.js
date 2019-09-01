$package("com.kidscademy.atlas");

com.kidscademy.atlas.GraphicAssets = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

    	/**
    	 * Parent form page.
    	 * @type {com.kidscademy.atlas.FormPage}
    	 */
		this._formPage = null;

		/**
		 * List control for pictures.
		 * @type {com.kidscademy.atlas.PicturesControl}
		 */
		this._picturesControl = this.getByClass(com.kidscademy.atlas.PicturesControl);
		this._picturesControl.on("picture-selected", this._onPictureSelected, this);

		/**
		 * Form data that holds meta about image, like name, source and caption.
		 * @type {com.kidscademy.FormData}
		 */
		this._metaFormData = this.getByCssClass("meta");

		/**
		 * Image editor has two sections: info view - both file and crop area info, and image preview with 
		 * crop mask. It control visibility of its children.
		 * @type {js.dom.Editor}
		 */
		this._imageEditor = this.getByCssClass("image-editor");

		/**
		 * File info view display information about image file. It is a child of {@link #_imageEditor}.
		 * @type {js.dom.Element}
		 */
		this._fileInfoView = this.getByCssClass("file-info");

		/**
		 * Crop info view display information about crop area, e.g. position and dimmensions. It is a child of {@link #_imageEditor}.
		 * @type {js.dom.Element}
		 */
		this._cropInfoView = this.getByCssClass("crop-info");

		/**
		 * Image element that display the actual image preview.
		 * @type {js.dom.Image}
		 */
		this._previewImage = this.getByCss(".preview img");
		this._previewImage.on("load", this._onPreviewImageLoad, this);

		/**
		 * Crop mask is used by user to select crop area. It is overlayed on {@link #_previewImage}.
		 * @type {com.kidscademy.atlas.CropMask}
		 */
		this._cropMask = this.getByClass(com.kidscademy.atlas.CropMask);

		/**
		 * Picture object currently on image editor. This object contain information about file and meta about content.
		 * @type {Object}
		 */
		this._currentPicture = null;

		/**
		 * Number for transforms perfomed on current picture per current working session. This value is incremented for each 
		 * transform and decresed on undo.
		 * @type {Number}
		 */
		this._transformsCount = 0;

		/**
		 * Images search and its result list view.
		 * @type {js.dom.Element}
		 */
		//this._imageSearch = this.getByClass(com.kidscademy.atlas.ImageSearch);
		//this._imageSearch.on("selected", this._onSearchSelected, this);

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this._actions.showOnly("add");
		// register event for hidden input of type file to trigger image loading from host OS
		this.getByName("upload-file").on("change", this._onUploadFile, this);
	}

	onCreate(formPage) {
		this._formPage = formPage;
	}

	onStart() {
		const object = this._formPage.getObject();
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onAdd() {
		// this._actions.show("upload", "search", "link", "close");
		this._actions.show("upload", "link", "close");
		this._imageEditor.hide();
		this._metaFormData.open();
		this._metaFormData.enable("name");
	}

	_onUpload(ev) {
		const object = this._formPage.getObject();
		if (!object.name) {
			js.ua.System.alert("Missing object name.");
			ev.halt();
			return;
		}

		if (!this._metaFormData.isValid()) {
			// stop bubbling and default behavior for click event
			// by doing this 'change' event for inner file input is not longer fired
			// and next _onUploadFile is not invoked
			ev.halt();
			return;
		}

		// here object name exists and meta form data is valid, so we can upload media file
		// after this click handler exit, 'change' event for inner file input is fired
		// and next _onUploadFile is executed

		this._metaFormData.hide();
	}

	_onUploadFile(ev) {
		const formData = this._metaFormData.getFormData();
		const object = this._formPage.getObject();

		formData.append("object-id", object.id);
		formData.append("object-dtype", object.dtype);
		formData.append("object-name", object.name);
		formData.append("media-file", ev.target._node.files[0]);

		AtlasService.uploadPicture(formData, picture => {
			this._currentPicture = picture;
			this._picturesControl.addPicture(picture);
			this._previewImage.setSrc(picture.src);
		});
	}

	_onSearch() {
		if (this._metaFormData.isValid()) {
			this._imageSearch.open(result => {
				this._metaFormData.setValue("source", result.source);
				this._onLink();
			});
		}
	}

	_onLink() {
		if (!this._metaFormData.isValid()) {
			return;
		}
		const formData = this._metaFormData.getFormData();
		const object = this._formPage.getObject();

		formData.append("object-id", object.id);
		formData.append("object-dtype", object.dtype);
		formData.append("object-name", object.name);

		AtlasService.uploadPictureBySource(formData, picture => {
			this._currentPicture = picture;
			this._picturesControl.addPicture(picture);
			this._previewImage.setSrc(picture.src);
		});
	}

	_onDuplicate() {
		this._metaFormData.enable("name");
		this._metaFormData.show();
	}

	_onEdit() {
		this._metaFormData.show(!this._metaFormData.isVisible());
	}

	_onCrop() {
		var aspectRatio = 0;
		switch (this._currentPicture.name) {
			case "icon":
				aspectRatio = 1;
				break;

			case "contextual":
				aspectRatio = 1.6428;
				break;
		}

		this._cropMask.open({
			width: this._previewImage._node.width,
			height: this._previewImage._node.height,
			naturalWidth: this._previewImage._node.naturalWidth,
			naturalHeight: this._previewImage._node.naturalHeight,
			aspectRatio: aspectRatio
		}, this._onCropUpdate, this);
	}

	/**
	 * Invoked in real time when user changes crop area. It gets data about crop area position and dimensions
	 * and display it on {@link #_cropInfoView}.
	 * 
	 * @param {Object} cropInfo crop area info. 
	 */
	_onCropUpdate(cropInfo) {
		this._cropInfoView.setObject(cropInfo);
	}

	_onTrim() {
		AtlasService.trimPicture(this._formPage.getUIObject(), this._currentPicture, this._onProcessingDone, this);
	}

	_onFlop() {
		AtlasService.flopPicture(this._formPage.getUIObject(), this._currentPicture, this._onProcessingDone, this);
	}

	_onFlip() {
		AtlasService.flipPicture(this._formPage.getUIObject(), this._currentPicture, this._onProcessingDone, this);
	}

	/**
	 * Commit current operation. Done button action depends on context. If current operation is <code>duplicate</code> this
	 * handler execute it on server.
	 */
	_onDone() {
		switch (this._actions.getPreviousAction()) {
			case "duplicate":
				const duplicatePicture = this._metaFormData.getObject();
				duplicatePicture.fileName = this._currentPicture.fileName;
				duplicatePicture.src = this._currentPicture.src;
				AtlasService.duplicatePicture(this._formPage.getUIObject(), duplicatePicture, picture => {
					this._currentPicture = picture;
					this._picturesControl.addPicture(picture);
					this._metaFormData.hide();
					this._closeImageEditor();
				});
				break;

			case "crop":
				const crop = this._cropMask.getCropArea();
				this._cropMask.hide();
				AtlasService.cropPicture(this._formPage.getUIObject(), this._currentPicture, crop.cx, crop.cy, crop.x, crop.y, this._onProcessingDone, this);
				break;

			default:
				this._metaFormData.getObject(this._currentPicture);
				AtlasService.commitPicture(this._formPage.getUIObject(), this._currentPicture, picture => {
					this._metaFormData.hide();
					this._closeImageEditor();
					this._picturesControl.updatePicture(picture);
				})
		}
	}

	_onUndo() {
		AtlasService.undoPicture(this._formPage.getUIObject(), this._currentPicture, picture => {
			--this._transformsCount;
			this._currentPicture = picture;
			this._previewImage.reload(picture.src);
		});
	}

	_onClose() {
		if (this._transformsCount === 0) {
			this._metaFormData.hide();
			this._closeImageEditor();
			return;
		}
		js.ua.System.confirm("@string/confirm-picture-rollback", answer => {
			if (answer === true) {
				AtlasService.rollbackPicture(this._formPage.getUIObject(), this._currentPicture, this._closeImageEditor, this);
			}
		});
	}

	_onRemove() {
		js.ua.System.confirm("@string/confirm-picture-remove", answer => {
			if (answer === true) {
				AtlasService.removePicture(this._formPage.getUIObject(), this._currentPicture, () => {
					this._closeImageEditor();
					this._picturesControl.removePicture(this._currentPicture);
				});
			}
		});
	}

	// --------------------------------------------------------------------------------------------

	_closeImageEditor() {
		this._actions.showOnly("add");
		this._metaFormData.hide();
		this._imageEditor.hide();
	}

	/**
	 * Callback invoked when server picture processing is complete.
	 * 
	 * @param {Object} picture picture returned by server.
	 */
	_onProcessingDone(picture) {
		++this._transformsCount;
		this._currentPicture = picture;
		this._previewImage.reload(picture.src);
	}

	_onPreviewImageLoad(ev) {
		this._transformsCount = 0;
		this._actions.showAll().hide("add", "upload", "search", "link");
		this._cropMask.hide();
		this._imageEditor.show();
		this._metaFormData.setObject(this._currentPicture);
		this._fileInfoView.setObject(this._currentPicture);
	}

	_onPictureSelected(picture) {
		this._actions.show("remove");
		this._metaFormData.disable("name");
		this._currentPicture = picture;
		this._previewImage.setSrc(picture.src);
	}

	/**
	* Class string representation.
	* 
	* @return this class string representation.
	*/
	toString() {
		return "com.kidscademy.atlas.GraphicAssets";
	}
}
