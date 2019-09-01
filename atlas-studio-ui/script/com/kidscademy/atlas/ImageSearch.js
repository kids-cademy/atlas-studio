$package("com.kidscademy.atlas");

com.kidscademy.atlas.ImageSearch = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Google service API key. 
		 * @type {String}
		 */
		this._KEY = "AIzaSyAEGN5tqyNNGvXDVJHlRKOD5hKFdzTBXHA";

		/**
		 * Context ID from Google custom search engine.
		 * @type {String}
		 */
		this._CX = "013439824093593264151:4j4_1h_gwac";

		/**
		 * Images result size. This value is currently limited by service provider to 10.
		 * @type {Number}
		 */
		this._RESULT_SIZE = 10;

		/**
		 * Timeout for image loading.
		 * @type {Number}
		 */
		this._IMAGE_LOADING_TIMEOUT = 2000;

		this._resultImagesView = this.getByCssClass("result-images");
		this._resultImagesView.on("click", this._onImageClick, this);
		this._resultImagesView.on("dragstart", this._onImageDragStart, this);
		this._ownerDoc.on("dragover", this._onDocumentDragOver, this);
		this._ownerDoc.on("drop", this._onDocumentDrop, this);

		this._imagePreview = this.getByCssClass("image-preview");

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this._actions.hide("done");

		this._queryInput = this._actions.getControl("query");

		this._selectedImageMeta = null;
	}

	open(callback) {
		this._callback = callback;
		this.show();
		this.scrollIntoView();
	}

	hide() {
		this._query = null;
		super.hide();
	}

	_onNextPage() {
		if (this._queryInput.isValid()) {
			this._resultImagesView.removeChildren();
			this.search(this._queryInput.getValue());
		}
	}

	_onPreviousPage() {
		if (this._queryInput.isValid()) {
			this._resultImagesView.removeChildren();
			this.search(this._queryInput.getValue(), /* move back to previous page */ true);
		}
	}

	_onAdd() {
		if (this._queryInput.isValid()) {
			this.search(this._queryInput.getValue());
		}
	}

	_onClose() {
		this.hide();
	}

	_onDone() {
		this.hide();
		this._callback(this._selectedImageMeta);
	}

	search(query, previousPage = false) {
		// index of the first result to return is one based
		// see https://developers.google.com/custom-search/v1/cse/list

		if (!this._context || this._context.query !== query) {
			// fresh search
			this._context = {
				start: 1,
				query: query,
			}
		}
		else {
			// repeat last search but moving to next result page
			// anyway, if previous page flag is present and true, move back to previous page
			if (previousPage) {
				this._context.start = Math.max(this._context.start - this._RESULT_SIZE, 1);
			}
			else {
				this._context.start += this._RESULT_SIZE;
			}
		}

		this._search(this._context);
	}

	_search(context) {
		//const request = `https://www.googleapis.com/customsearch/v1?key=${this._KEY}&cx=${this._CX}&num=${this._RESULT_SIZE}&start=${context.start}&q=${context.query}&searchType=image&rights=cc_publicdomain&imgSize=xxlarge`;

		const request = js.net.URL("https://www.googleapis.com/customsearch/v1", {
			key: this._KEY,
			cx: this._CX,
			num: this._RESULT_SIZE,
			start: context.start,
			q: context.query,
			searchType: "image",
			rights: "cc_publicdomain",
			imgSize: "xxlarge"
		});

		const xhr = new js.net.XHR();

		xhr.on('load', result => {
			if (!result.items) {
				js.ua.System.alert("Oops! No images found.");
				return;
			}

			this.show();
			context.items = result.items;
			context.itemsIndex = 0;
			this._loadImage(context);
		});

		xhr.open(js.net.Method.GET, request);
		xhr.send();
	}

	_loadImage(context) {
		if (context.itemsIndex === context.items.length) {
			// all result images are loaded
			return;
		}

		const item = context.items[context.itemsIndex];
		const imageElement = this._ownerDoc.createElement("img");

		const loadHandler = () => {
			imageElement.setUserData("meta", {
				//name: item.title,
				mediaType: item.mime,
				fileSize: item.image.byteSize,
				width: item.image.width,
				height: item.image.height,
				src: item.link,
				source: item.link,
				uploadDate: new Date()
			});
			this._resultImagesView.addChild(imageElement);
			// fall through error handler
			errorHandler();
		};

		const errorHandler = () => {
			clearTimeout(timer);
			imageElement.un("load", loadHandler);
			imageElement.un("error", errorHandler);
			++context.itemsIndex;
			this._loadImage(context);
		};

		imageElement.on("load", loadHandler, this);
		imageElement.on("error", errorHandler, this);

		const timer = setTimeout(() => {
			errorHandler();
			imageElement.reset();
		}, this._IMAGE_LOADING_TIMEOUT);

		imageElement.setSrc(item.image.thumbnailLink);
	}

	_onImageClick(ev) {
		if (ev.target.getTag() === "img") {
			this._selectedImageMeta = ev.target.getUserData("meta");
			this._imagePreview.setSrc(this._selectedImageMeta.source);
			this._actions.show("done");
		}
	}

	_onImageDragStart(ev) {
		ev.setData("images-search-index", ev.target.getChildIndex());
	}

	_onDocumentDragOver(ev) {
		ev.prevent();
	}

	_onDocumentDrop(ev) {
		ev.prevent();
		const index = Number(ev.getData("images-search-index"));
		if (!isNaN(index)) {
			const image = this._resultImagesView.getByIndex(index);
			if (this._selectedImageMeta === image.getUserData("meta")) {
				this._imagePreview.reset();
			}
			image.remove();
		}
	}

	toString() {
		return "com.kidscademy.atlas.ImageSearch";
	}
};
