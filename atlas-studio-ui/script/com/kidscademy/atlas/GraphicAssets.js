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
		 * Custom list control for images.
		 * @type {com.kidscademy.atlas.ImagesControl}
		 */
		this._imagesControl = this.getByClass(com.kidscademy.atlas.ImagesControl);
		this._imagesControl.on("image-selected", this._onImageSelected, this);

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
		 * Image object currently on image editor. This object contain information about file and meta about content.
		 * @type {Object}
		 */
		this._currentImage = null;

		/**
		 * Number of transforms perfomed on current image per current working session. This value is incremented for each 
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
		this._metaFormData.enable("image-key");
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

		formData.append("atlas-object-id", object.id);
		formData.append("media-file", ev.target._node.files[0]);

		AtlasService.uploadImage(formData, image => {
			this._currentImage = image;
			this._imagesControl.addImage(image);
			this._previewImage.setSrc(image.src);
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

		formData.append("atlas-object-id", object.id);

		AtlasService.uploadImageBySource(formData, image => {
			this._currentImage = image;
			this._imagesControl.addImage(image);
			this._previewImage.setSrc(image.src);
		});
	}

	_onDuplicate() {
		this._metaFormData.enable("image-key");
		this._metaFormData.show();
	}

	_onEdit() {
		this._metaFormData.show(!this._metaFormData.isVisible());
	}

	_onCrop() {
		var aspectRatio = 0;
		switch (this._currentImage.imageKey) {
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
		AtlasService.trimImage(this._formPage.getAtlasItem(), this._currentImage, this._onProcessingDone, this);
	}

	_onFlop() {
		AtlasService.flopImage(this._formPage.getAtlasItem(), this._currentImage, this._onProcessingDone, this);
	}

	_onFlip() {
		AtlasService.flipImage(this._formPage.getAtlasItem(), this._currentImage, this._onProcessingDone, this);
	}

	/**
	 * Commit current operation. Done button action depends on context. If current operation is <code>duplicate</code> this
	 * handler execute it on server.
	 */
	_onDone() {
		switch (this._actions.getPreviousAction()) {
			case "duplicate":
				const duplicateImage = this._metaFormData.getObject();
				duplicateImage.fileName = this._currentImage.fileName;
				duplicateImage.src = this._currentImage.src;
				AtlasService.duplicateImage(this._formPage.getAtlasItem(), duplicateImage, image => {
					this._currentImage = image;
					this._imagesControl.addImage(image);
					this._metaFormData.hide();
					this._closeImageEditor();
				});
				break;

			case "crop":
				const crop = this._cropMask.getCropArea();
				this._cropMask.hide();
				AtlasService.cropImage(this._formPage.getAtlasItem(), this._currentImage, crop.cx, crop.cy, crop.x, crop.y, this._onProcessingDone, this);
				break;

			default:
				this._metaFormData.getObject(this._currentImage);
				AtlasService.commitImage(this._formPage.getAtlasItem(), this._currentImage, image => {
					this._metaFormData.hide();
					this._closeImageEditor();
					this._imagesControl.updateImage(image);
				})
		}
	}

	_onUndo() {
		AtlasService.undoImage(this._formPage.getAtlasItem(), this._currentImage, image => {
			--this._transformsCount;
			this._currentImage = image;
			this._previewImage.reload(image.src);
		});
	}

	_onClose() {
		if (this._transformsCount === 0) {
			this._metaFormData.hide();
			this._closeImageEditor();
			return;
		}
		js.ua.System.confirm("@string/confirm-image-rollback", answer => {
			if (answer === true) {
				AtlasService.rollbackImage(this._formPage.getAtlasItem(), this._currentImage, this._closeImageEditor, this);
			}
		});
	}

	_onRemove() {
		js.ua.System.confirm("@string/confirm-image-remove", answer => {
			if (answer === true) {
				AtlasService.removeImage(this._formPage.getAtlasItem(), this._currentImage, () => {
					this._closeImageEditor();
					this._imagesControl.removeImage(this._currentImage);
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
	 * Callback invoked when server image processing is complete.
	 * 
	 * @param {Object} image image returned by server.
	 */
	_onProcessingDone(image) {
		++this._transformsCount;
		this._currentImage = image;
		this._previewImage.reload(image.src);
	}

	_onPreviewImageLoad(ev) {
		this._transformsCount = 0;
		this._actions.showAll().hide("add", "upload", "search", "link");
		this._cropMask.hide();
		this._imageEditor.show();
		this._metaFormData.setObject(this._currentImage);
		this._fileInfoView.setObject(this._currentImage);
	}

	_onImageSelected(image) {
		this._actions.show("remove");
		this._metaFormData.disable("image-key");
		this._currentImage = image;
		this._previewImage.setSrc(image.src);
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
