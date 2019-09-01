$package("com.kidscademy.atlas");

com.kidscademy.atlas.CropMask = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._MOVE = 0;
		this._RESIZE_W = 1;
		this._RESIZE_E = 2;
		this._RESIZE_N = 3;
		this._RESIZE_S = 4;

		this._x = 0;
		this._y = 0;
		this._cx = 0;
		this._cy = 0;

		this._moveType = null;

		this._mouseX = 0;
		this._mouseY = 0;

		this._imageWidth = 0;
		this._imageHeight = 0;

		this._xOffsetRect = this.getByCssClass("x-offset");
		this._yOffsetRect = this.getByCssClass("y-offset");
		this._cropColumn = this.getByCssClass("crop-column");
		this._cropRect = this.getByCssClass("crop-rect");

		this.on("mousedown", this._onMouseDown, this);

		/**
		 * Frame request callback for box rendering while moving.
		 * @type {Function}
		 */
		this._FRAME_REQUEST_CALLBACK = this._updateSelectArea.bind(this);
	}

	open(config, callback, scope) {
		this._imageWidth = config.width;
		this._imageHeight = config.height;
		this._px = config.naturalWidth / config.width;
		this._py = config.naturalHeight / config.height;
		this._aspectRatio = config.aspectRatio;

		this._cy = 300;
		this._cx = this._aspectRatio ? this._aspectRatio * this._cy : 400;

		this._callback = callback;
		this._scope = scope;

		this.show();
		this._updateSelectArea();
	}

	getCropArea() {
		return {
			x: this._x * this._px,
			y: this._y * this._py,
			cx: this._cx * this._px,
			cy: this._cy * this._py
		}
	}

	_onMouseDown(ev) {
		ev.halt();

		if (ev.target.hasCssClass("w")) {
			this._moveType = this._RESIZE_W;
		}
		else if (ev.target.hasCssClass("e")) {
			this._moveType = this._RESIZE_E;
		}
		else if (ev.target.hasCssClass("n")) {
			this._moveType = this._RESIZE_N;
		}
		else if (ev.target.hasCssClass("s")) {
			this._moveType = this._RESIZE_S;
		}
		else {
			this._moveType = this._MOVE;
		}

		this._mouseX = ev.pageX;
		this._mouseY = ev.pageY;

		this.addCssClass("moving");
		this._ownerDoc.on("mousemove", this._onMouseMove, this);
		this._ownerDoc.on("mouseup", this._onMouseUp, this);
	}

	_onMouseMove(ev) {
		ev.halt();

		var dx = ev.pageX - this._mouseX;
		var dy = ev.pageY - this._mouseY;

		this._mouseX = ev.pageX;
		this._mouseY = ev.pageY;

		switch (this._moveType) {
			case this._MOVE:
				this._x = Math.min(Math.max(this._x + dx, 0), this._imageWidth - this._cx);
				this._y = Math.min(Math.max(this._y + dy, 0), this._imageHeight - this._cy);
				break;

			case this._RESIZE_W:
				if (this._x + dx <= 0) {
					dx = -this._x;
					this._x = 0;
				}
				else {
					this._x += dx;
				}
				this._cx -= dx;
				if (this._aspectRatio) {
					this._cy = this._cx / this._aspectRatio;
				}
				break;

			case this._RESIZE_E:
				this._cx += dx;
				if (this._aspectRatio) {
					this._cy = this._cx / this._aspectRatio;
				}
				break;

			case this._RESIZE_N:
				if (this._y + dy <= 0) {
					dy = -this._y;
					this._y = 0;
				}
				else {
					this._y += dy;
				}
				this._cy -= dy;
				if (this._aspectRatio) {
					this._cx = this._cy * this._aspectRatio;
				}
				break;

			case this._RESIZE_S:
				this._cy += dy;
				if (this._cy > this._imageHeight) {
					this._cy = this._imageHeight;
				}
				if (this._aspectRatio) {
					this._cx = this._cy * this._aspectRatio;
				}
				break;
		}

		requestAnimationFrame(this._FRAME_REQUEST_CALLBACK);
	}

	_onMouseUp(ev) {
		ev.halt();
		this._ownerDoc.un("mousemove", this._onMouseMove);
		this._ownerDoc.un("mouseup", this._onMouseUp);
		this.removeCssClass("moving");
	}

	_updateSelectArea() {
		this._xOffsetRect.style.set("flex-basis", this._x + "px");
		this._yOffsetRect.style.set("flex-basis", this._y + "px");

		this._cropColumn.style.set("flex-basis", this._cx + "px");
		this._cropRect.style.set("flex-basis", this._cy + "px");

		this._callback.call(this._scope, {
			left: Math.round(this._x * this._px),
			top: Math.round(this._y * this._py),
			width: Math.round(this._cx * this._px),
			height: Math.round(this._cy * this._py)
		});
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.CropMask";
	}
};

$preload(com.kidscademy.atlas.CropMask);
