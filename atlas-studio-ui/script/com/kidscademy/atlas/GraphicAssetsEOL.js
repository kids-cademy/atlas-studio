$package("com.kidscademy.atlas");

com.kidscademy.atlas.GraphicAssets = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

    	/**
    	 * Parent form page.
    	 * @type {com.kidscademy.atlas.FormPage}
    	 */
		this._formPage = null;

		this._pictureImage = this.getByName("picture-src");
		this._iconImage = this.getByName("icon-src");
		this._thumbnailImage = this.getByName("thumbnail-src");

		this.getByName("upload-file").on("change", this._onUploadFile, this);

		this._fileView = this.getByCssClass("file-view");
		this._cropView = this.getByCssClass("crop-view");
		this._previewImage = this.getByCss(".preview img");
		this._previewImage.on("load", this._onPreviewImageLoad, this);

		this._pictureNameInput = this.getByCssClass("picture-name");
		this._pictureTypeSelect = this.getByCssClass("picture-type");

		this._metaFormData = this.getByCssClass("meta");
		this._imageEditor = this.getByCssClass("editor");
		this._cropEditor = this.getByCssClass("crop-mask");

		this._aspectRatio = 0;

		this._picturesView = this.getByClass(com.kidscademy.atlas.PicturesControl);

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this._actions.hide("upload", "search", "edit");
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
		this._actions.show("upload", "search", "edit");
		this._metaFormData.show();
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
		const formData = this._metaFormData.getObject();
		const object = this._formPage.getObject();

		formData.append("object-dtype", object.dtype);
		formData.append("object-name", object.name);
		formData.append("media-file", ev.target._node.files[0]);

		AtlasService.uploadObjectMediaFile(formData, picture => {
			this._picturesView.addPicture(picture);
			this._previewImage.setSrc(picture.src);
		});
	}

	_onEdit() {
		this._metaFormData.show(this._metaFormData.hasCssClass("hidden"));
	}

	_onCropSquare() {
		this._aspectRatio = 1;
		this._onCrop();
	}

	_onCropLandscape() {
		this._aspectRatio = 16 / 9;
		this._onCrop();
	}

	_onCropFree() {
		this._aspectRatio = 0;
		this._onCrop();
	}

	_onCrop() {
		this._cropEditor.open({
			width: this._previewImage._node.width,
			height: this._previewImage._node.height,
			naturalWidth: this._previewImage._node.naturalWidth,
			naturalHeight: this._previewImage._node.naturalHeight,
			aspectRatio: this._aspectRatio
		}, this._onCropUpdate, this);
	}

	_onCropUpdate(cropInfo) {
		this._cropView.setObject(cropInfo)
	}

	_onIcon() {
		this._iconImage.removeCssClass("invalid");
		const object = this._formPage.getObject();
		if (!object.name) {
			js.ua.System.alert("Missing object name.");
			return;
		}
		AtlasService.createObjectIcon(object.dtype, object.name, (iconSrc) => this._iconImage.setValue(iconSrc));
	}

	_onFlip() {
		js.ua.System.alert("Vertical flip not yet implemented.");
	}

	_onInvert() {
		js.ua.System.alert("Invert not yet implemented.");
	}

	_onDone() {
		const meta = this._getMeta();
		if (!meta) {
			return;
		}

		const crop = this._cropEditor.getCropArea();
		this._cropEditor.hide();

		const object = this._formPage.getUIObject();
		const imageName = "picture.jpg";
		AtlasService.cropObjectImage(object, imageName, crop.cx, crop.cy, crop.x, crop.y, function (imageSrc) {
			this._previewImage.setSrc(imageSrc);
		}, this);
	}

	_onDoneOnCanvas() {
		const crop = this._cropEditor.getCropArea();
		this._cropEditor.hide();

		const canvas = this.getByTag("canvas")._node;
		canvas.width = crop.cx;
		canvas.height = crop.cy;

		const context = canvas.getContext("2d");
		context.drawImage(this._previewImage._node, crop.x, crop.y, crop.cx, crop.cy, 0, 0, crop.cx, crop.cy);

		this._previewImage.setSrc(canvas.toDataURL());
		this._canvasUpdated = true;
	}

	_onUndo() {
		js.ua.System.alert("Undo not yet implemented.");
	}

	_onSave() {
		if (!this._canvasUpdated) {
			const canvas = this.getByTag("canvas")._node;
			canvas.width = this._previewImage._node.naturalWidth;
			canvas.height = this._previewImage._node.naturalHeight;

			const context = canvas.getContext("2d");
			context.drawImage(this._previewImage._node, 0, 0);
		}

		switch (this._aspectRatio) {
			case 0:
				this._upload("upload-thumbnail-file", thumbnailSrc => this._thumbnailImage.setValue(thumbnailSrc));
				break;

			case 1:
				this._upload("upload-icon-file", iconSrc => this._iconImage.setValue(iconSrc));
				break;

			default:
				this._upload("upload-picture-file", pictureSrc => this._pictureImage.setValue(pictureSrc));
		}
	}

	_onClose() {
		this._metaFormData.hide();
		this._imageEditor.hide();
		this._actions.hide("upload", "search");
	}

	// --------------------------------------------------------------------------------------------

	_onSearch() {
		alert('search')
	}

	_onPictureUpload(ev) {
		this._pictureImage.removeCssClass("invalid");
		this._aspectRatio = 16 / 9;
		this._onImageFileSelected(ev);
	}

	_onIconUpload(ev) {
		this._iconImage.removeCssClass("invalid");
		this._aspectRatio = 1;
		this._onImageFileSelected(ev);
	}

	_onThumbnailUpload(ev) {
		this._thumbnailImage.removeCssClass("invalid");
		this._aspectRatio = 0;
		this._onImageFileSelected(ev);
	}

	_upload(method, callback) {
		const object = this._formPage.getObject();
		if (!object.name) {
			js.ua.System.alert("Missing object name.");
			return;
		}

		const xhr = new js.net.XHR();
		xhr.on("load", callback, this);
		xhr.open("POST", "rest/" + method);

		const canvas = this.getByTag("canvas")._node;
		canvas.toBlob((file) => {
			const data = new FormData();
			data.append("dtype", object.dtype);
			data.append("name", object.name);
			data.append("file", file);

			xhr.send(data);
		}, this._imageFile.type);
	}

	_onImageFileSelected(ev) {
		this._canvasUpdated = false;

		this._imageFile = ev.target._node.files[0];
		const reader = new FileReader();
		reader.onload = () => this._previewImage.setSrc(reader.result);
		reader.readAsDataURL(this._imageFile);
	}

	_onPreviewImageLoad(ev) {
		this._cropEditor.hide();
		this._imageEditor.show();
		//this._imageFile.width = this._previewImage._node.naturalWidth;
		//this._imageFile.height = this._previewImage._node.naturalHeight;
		this._fileView.setObject(this._imageFile);
	}

	_getMeta() {
		var valid = this._pictureNameInput.isValid();
		valid = this._pictureTypeSelect.isValid() && valid;
		if (!valid) {
			return null;
		}
		return {
			name: this._pictureNameInput.getValue(),
			type: this._pictureTypeSelect.getValue()
		}
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
