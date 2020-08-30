$package("com.kidscademy");

com.kidscademy.ImageEditor = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._aspectRatio = 0;

		/**
		 * Number of transforms performed on current image per current working session. This value is incremented for each 
		 * transform and decreased on undo.
		 * @type {Number}
		 */
        this._transformsCount = 0;

		/**
		 * Image editor has two sections: info view - both file and crop area info, and image preview with 
		 * crop mask. It control visibility of its children.
		 * @type {js.dom.Element}
		 */
        this._editor = this.getByCssClass("editor");

		/**
		 * File info view display information about image file. It is a child of {@link #_imageEditor}.
		 * @type {js.dom.Element}
		 */
        this._fileInfoView = this.getByCssClass("file-info");

		/**
		 * Crop info view display information about crop area, e.g. position and dimensions. It is a child of {@link #_imageEditor}.
		 * @type {js.dom.Element}
		 */
        this._cropInfoView = this.getByCssClass("crop-info");

        /**
         * Image element that display the actual image preview.
         * @type {com.kidscademy.ImageView}
         */
        this._preview = this.getByClass(com.kidscademy.ImageView);
        this._preview.on("load", this._onPreviewLoaded, this);

		/**
		 * Crop mask is used by user to select crop area. It is overlayed on {@link #_previewImage}.
		 * @type {com.kidscademy.CropMask}
		 */
        this._cropMask = this.getByClass(com.kidscademy.CropMask);

        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
        this._actions.showOnly("add");
        // register event for hidden input of type file to trigger image loading from host OS
        this._actions.getByName("file-upload").on("change", this._onFileUpload, this);

        /**
         * Optional action arguments form.
         * @type {com.kidscademy.FormData}
         */
        this._argsForm = null;

        this._events = this.getCustomEvents();
        this._events.register("image-add", "image-upload", "image-save", "image-link", "image-meta", "image-remove");
    }

    config(config) {
        this._aspectRatio = config.aspectRatio;
    }

    setAspectRatio(aspectRatio) {
        this._aspectRatio = aspectRatio;
    }

    /**
     * Open a new editing session.
     * 
     * @param {Function} callback callback function invoked on image editor close,
     * @param {Object} image optional image descriptor, default to null. 
     */
    open(callback, image = null) {
        this._callback = callback;
        if (image != null) {
            this._preview.setImage(image);
        }
        this._ownerDoc.on("paste", this._onImagePaste, this);
    }

    close(image = null) {
        this._ownerDoc.un("paste", this._onImagePaste);
        this._callback(image);
        this._callback = null;
        this._actions.showOnly("add");
        this._editor.hide();
        this._preview.reset();
        this._transformsCount = 0;
    }

    getActions() {
        return this._actions;
    }

    getImage() {
        return this._preview.getImage();
    }

    isVisible() {
        return this._editor.isVisible();
    }

    // --------------------------------------------------------------------------------------------
    // IMAGE LIFE CYCLE HANDLERS

    _onAdd() {
		this._actions.showOnly("image-upload", "image-link", "close");
        this._events.fire("image-add");
    }

    _onImageUpload(ev) {
        this._events.fire("image-upload", ev);
		this._actions.showOnly("add");
    }

    _onFileUpload(ev) {
        const file = ev.target._node.files[0];
        if (file) {
            ev.halt();
            this._events.fire("image-save", file);
        }
    }

    _onImagePaste(ev) {
        const file = ev.clipboardData("image");
        if (file) {
            ev.halt();
            this._events.fire("image-save", file);
        }
    }

    _onImageLink() {
        this._events.fire("image-link");
    }

    _onMetaForm() {
        this._events.fire("image-meta");
    }

    _onImageRemove() {
        this._events.fire("image-remove");
    }

    // --------------------------------------------------------------------------------------------
    // IMAGE TRANSFORM HANDLERS

    _onCropFree() {
        this._cropMask.open({
            type: "FREE",
            width: this._preview._node.width,
            height: this._preview._node.height,
            naturalWidth: this._preview._node.naturalWidth,
            naturalHeight: this._preview._node.naturalHeight,
            aspectRatio: this._aspectRatio
        }, this._onCropUpdate, this);
    }

    _onCropCircle(argsForm) {
        this._argsForm = argsForm;
        this._cropMask.open({
            type: "CIRCLE",
            width: this._preview._node.width,
            height: this._preview._node.height,
            naturalWidth: this._preview._node.naturalWidth,
            naturalHeight: this._preview._node.naturalHeight,
            aspectRatio: 1
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
        ImageService.trimImage(this._preview.getImage(), this._onProcessingDone, this);
    }

    _onCanvasSize(argsForm) {
        this._argsForm = argsForm;
    }

    _onFlop() {
        ImageService.flopImage(this._preview.getImage(), this._onProcessingDone, this);
    }

    _onFlip() {
        ImageService.flipImage(this._preview.getImage(), this._onProcessingDone, this);
    }

    _onRotate(argsForm) {
        this._argsForm = argsForm;
    }

    _onBrightnessContrast(argsForm) {
        this._argsForm = argsForm;
    }

	/**
	 * Commit current operation. Done button action depends on context. If current operation is <code>duplicate</code> this
	 * handler execute it on server.
	 */
    _onDone() {
        var args, crop, height;
        switch (this._actions.getPreviousAction()) {
            case "crop-free":
                crop = this._cropMask.getCropArea();
                this._cropMask.hide();
                ImageService.cropRectangleImage(this._preview.getImage(), crop.cx, crop.cy, crop.x, crop.y, this._onProcessingDone, this);
                break;

            case "crop-circle":
                crop = this._cropMask.getCropArea();
                this._cropMask.hide();
                args = this._getActionArgs("crop-circle");
                ImageService.cropCircleImage(this._preview.getImage(), crop.cx, crop.cy, crop.x, crop.y, args.borderColor, Number(args.borderWidth), this._onProcessingDone, this);
                break;

            case "canvas-size":
                args = this._getActionArgs("rotate");
                height = Number(args.height ? args.height : args.width);
                ImageService.canvasSize(this._preview.getImage(), Number(args.width), height, args.gravity, this._onProcessingDone, this);
                break;

            case "rotate":
                args = this._getActionArgs("rotate");
                ImageService.rotateImage(this._preview.getImage(), args.direction, Number(args.angle), this._onProcessingDone, this);
                break;

            case "brightness-contrast":
                args = this._getActionArgs("brightness-contrast");
                ImageService.adjustBrightnessContrast(this._preview.getImage(), Number(args.brightness), Number(args.contrast), this._onProcessingDone, this);
                break;

            default:
                ImageService.commitImage(this._preview.getImage(), image => this.close(image));
        }
    }

    _onUndo() {
        ImageService.undoImage(this._preview.getImage(), image => {
            if (this._transformsCount > 0) {
                --this._transformsCount;
            }
            this._preview.setImage(image);
        });
    }

    _onClose() {
        if (this._argsForm != null) {
            this._argsForm = null;
            return;
        }

        if (this._transformsCount === 0) {
            this.close();
            return;
        }

        js.ua.System.confirm("@string/confirm-image-rollback", answer => {
            if (answer === true) {
                ImageService.rollbackImage(this._preview.getImage(), this.close, this);
            }
        });
    }

    // --------------------------------------------------------------------------------------------

    _getActionArgs(actionName) {
        $assert(this._argsForm != null, "com.kidscademy.ImageEditor#_getActionArgs", "Null arguments form for |%s| action.", actionName);
        const args = this._argsForm != null ? this._argsForm.getObject() : null;
        this._argsForm = null;
        return args;
    }

	/**
	 * Callback invoked when server image processing is complete. This method updates preview image and fires <change> event.
	 * 
	 * @param {Object} image image descriptor returned by server.
	 */
    _onProcessingDone(image) {
        ++this._transformsCount;
        this._preview.setImage(image);
    }

    _onPreviewLoaded(ev) {
        this._actions.showAll().hide("add");
        this._cropMask.hide();
        this._editor.show();
        this._fileInfoView.setObject(this._preview.getImage());
    }

    toString() {
        return "com.kidscademy.ImageEditor";
    }
};
