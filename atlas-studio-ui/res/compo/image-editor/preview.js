object = {
	id: 832,	
	collection: { name: "test" },
	name: "image-editor",
	images: [ {
			id: 1,
			imageKey: "icon",
			fileName: "icon.jpg",
			fileSize: 109661,
			width: 1112,
			height: 674,
			src: "/media/atlas/test/image-editor/icon.jpg"
		}, {
			id: 2,
			imageKey: "cover",
			fileName: "cover.png",
			fileSize: 109661,
			width: 1112,
			height: 674,
			src: "/media/atlas/test/image-editor/cover.png"
		}, {
			id: 3,
			imageKey: "context",
			fileName: "cougar.jpg",
			fileSize: 109661,
			width: 1112,
			height: 674,
			src: "/media/atlas/test/image-editor/cougar.jpg"
		}
	]
};

image = {
	fileName: "cougar.png",
	fileSize: 109661,
	width: 1112,
	height: 674
};

class IconControl extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._imageView = this.getByCssClass("image");
		this._imageView.on("click", this._onImageClick, this);
		
		this._metaForm = this.getByClass(com.kidscademy.FormData);

		this._imageEditor = this.getByClass(com.kidscademy.ImageEditor);
		this._imageEditor.config({ aspectRatio: 1.6 });
		
		this._imageEditor.on("open", this._onEditorOpen, this);
		this._imageEditor.on("close", this._onEditorClose, this);
		this._imageEditor.on("upload", this._onEditorUpload, this);
		this._imageEditor.on("link", this._onEditorLink, this);
		this._imageEditor.on("change", this._onEditorChange, this);
		this._imageEditor.on("remove", this._onEditorRemove, this);
	}
	
	_onImageClick(ev) {
		this._metaForm.open();
		image.src = ev.target.getAttr("src"); 
		this._imageEditor.open(image, image => {
			this._imageView.reload(image.src);
		});
	}
	
	_onEditorOpen() {
		this._metaForm.open();
	}
	
	_onEditorClose(image) {
		this._metaForm.hide();
	}
	
	_onEditorUpload(handler) {
        const formData = this._metaForm.getFormData();
		formData.append("image-kind", "OBJECT");
        formData.append("object-id", object.id);
        formData.append("media-file", handler.file);

        ImageService.uploadImage(formData, image => {
			this._imageView.reload(image.src);
			handler.callback(image);
        });
	}
	
	_onEditorLink(handler) {
        const formData = this._metaForm.getFormData();
		formData.append("image-kind", "OBJECT");
        formData.append("object-id", object.id);

        ImageService.uploadImageBySource(formData, image => {
			this._imageView.reload(image.src);
			handler.callback(image);
        });		
	}
	
	_onEditorChange(image) {
		this._metaForm.setObject(image);		
	}
	
	_onEditorRemove() {
		this._imageView.remove();
	}
	
	toString() {
		return "IconControl";
	}
};

class GraphicAssets extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._imagesList = this.getByName("images");
		this._imagesList.on("click", this._onImagesClick, this);
		
		this._metaForm = this.getByClass(com.kidscademy.FormData);
		
		this._imageEditor = this.getByClass(com.kidscademy.ImageEditor);
		
		this._actions = this._imageEditor._actions;
		this._actions.bind(this);
		
// this._actions.on("file", this._onFile, this);
// this._actions.on("link", this._onLink, this);
// this._actions.on("form", this._onForm, this);
// this._actions.on("remove", this._onRemove, this);
		
// this._imageEditor.on("open", this._onEditorOpen, this);
// this._imageEditor.on("close", this._onEditorClose, this);
// this._imageEditor.on("form", this._onEditorForm, this);
// this._imageEditor.on("upload", this._onEditorUpload, this);
// this._imageEditor.on("link", this._onEditorLink, this);
// this._imageEditor.on("change", this._onEditorChange, this);
// this._imageEditor.on("remove", this._onEditorRemove, this);
	}
	
	setObject(object) {
		// on server images are stored on a map with next keys: icon, cover, featured and contextual
		// on user interface images are displayed as a list view

		function addImage(image) {
			if (typeof image !== "undefined") {
				imagesList.push(image);
			}
		}

		var imagesList = [];
		addImage(object.images.icon);
		addImage(object.images.cover);
		addImage(object.images.trivia);
		addImage(object.images.featured);
		addImage(object.images.contextual);

		this._imagesList.setObject(imagesList);
		return this;
	}
	
	_onImagesClick(ev) {
		this._imageView = ev.target.getParentByTag("li");
		const image = this._imageView.getUserData();
		this._imageEditor.open(image, this._onEditorDone.bind(this));
	}
	
	_onEditorOpen() {
	}
	
	_onEditorClose(image) {
		this._metaForm.hide();
	}
	
	_onMetaForm() {
		this._metaForm.toggleCssClass("hidden");
	}
	
	_onEditorUpload(handler) {
        const formData = this._metaForm.getFormData();
		formData.append("image-kind", "OBJECT");
        formData.append("object-id", object.id);
        formData.append("media-file", handler.file);

		const method = handler.replace ? "replaceImage" : "uploadImage";
        ImageService[method](formData, image => {
			this._imageView.reload(image.src);
			handler.callback(image);
        });
	}
	
	_onImageLink() {
        const formData = this._metaForm.getFormData();
		formData.append("image-kind", "OBJECT");
        formData.append("object-id", object.id);

		const method = this._imageEditor.isVisible() ? "replaceImageBySource" : "uploadImageBySource";
        ImageService[method](formData, image => this._imageEditor.open(image, this._onEditorDone.bind(this)));		
	}
	
	_onEditorDone(image) {
		this._metaForm.hide();
		// if image editor aborts processing image argument is null
		if(image != null) {
			// image editor was closed with done and image processing commit
			
			// TODO: update images list
			// this._imageView.reload(image.src);
			alert(image.src)
		}
	}
	
	_onEditorChange(image) {
		this._metaForm.setObject(image);		
	}
	
	_onImageRemove() {
        js.ua.System.confirm("@string/confirm-image-remove", answer => {
            if (answer === true) {
        		this._imageView.remove();
                this._imageEditor.close();
            }
        });
	}
	
	toString() {
		return "GraphicAssets";
	}
};

WinMain.on("load", () => {
	const objectId = Number(WinMain.url.parameters.object);
	if(objectId) {
		AtlasService.getAtlasObject(objectId, o => {
			object = o;
			graphicAssets.setObject(o);
		});
	}
	
	const iconControl = WinMain.doc.getByClass(IconControl);
	const graphicAssets = WinMain.doc.getByClass(GraphicAssets);
	
	const buttonsBar = WinMain.doc.getByCssClass("buttons-bar");
	buttonsBar.getByName("icon-control").on("click", ev => {
		iconControl.show();
		graphicAssets.hide();
	});
	buttonsBar.getByName("graphic-assets").on("click", ev => {
		iconControl.hide();
		graphicAssets.setObject(object).show();
	});
});
