$package("com.kidscademy.atlas.studio");

/**
 * Image service.
 */
com.kidscademy.atlas.studio.ImageService = {
	/**
	 * Trim image.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 trimImage: function(image) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#trimImage", "Image argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#trimImage", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#trimImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "trimImage");
		rmi.setParameters(image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Flop image.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 flopImage: function(image) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#flopImage", "Image argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#flopImage", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#flopImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "flopImage");
		rmi.setParameters(image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Flip image.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 flipImage: function(image) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#flipImage", "Image argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#flipImage", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#flipImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "flipImage");
		rmi.setParameters(image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Rotate image.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param com.kidscademy.atlas.studio.model.RotateDirection direction,
	 * @param float angle,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 rotateImage: function(image, direction, angle) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#rotateImage", "Image argument is undefined.");
		$assert(typeof direction !== "undefined", "com.kidscademy.atlas.studio.ImageService#rotateImage", "Direction argument is undefined.");
		$assert(typeof angle !== "undefined", "com.kidscademy.atlas.studio.ImageService#rotateImage", "Angle argument is undefined.");
		$assert(js.lang.Types.isNumber(angle), "com.kidscademy.atlas.studio.ImageService#rotateImage", "Angle argument is not a number.");

		var __callback__ = arguments[3];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#rotateImage", "Callback is not a function.");
		var __scope__ = arguments[4];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#rotateImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "rotateImage");
		rmi.setParameters(image, direction, angle);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Crop rectangle image.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param int width,
	 * @param int height,
	 * @param int xoffset,
	 * @param int yoffset,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 cropRectangleImage: function(image, width, height, xoffset, yoffset) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Image argument is undefined.");
		$assert(typeof width !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Width argument is undefined.");
		$assert(js.lang.Types.isNumber(width), "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Width argument is not a number.");
		$assert(typeof height !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Height argument is undefined.");
		$assert(js.lang.Types.isNumber(height), "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Height argument is not a number.");
		$assert(typeof xoffset !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Xoffset argument is undefined.");
		$assert(js.lang.Types.isNumber(xoffset), "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Xoffset argument is not a number.");
		$assert(typeof yoffset !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Yoffset argument is undefined.");
		$assert(js.lang.Types.isNumber(yoffset), "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Yoffset argument is not a number.");

		var __callback__ = arguments[5];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Callback is not a function.");
		var __scope__ = arguments[6];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#cropRectangleImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "cropRectangleImage");
		rmi.setParameters(image, width, height, xoffset, yoffset);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Crop circle image.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param int width,
	 * @param int height,
	 * @param int xoffset,
	 * @param int yoffset,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 cropCircleImage: function(image, width, height, xoffset, yoffset) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Image argument is undefined.");
		$assert(typeof width !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Width argument is undefined.");
		$assert(js.lang.Types.isNumber(width), "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Width argument is not a number.");
		$assert(typeof height !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Height argument is undefined.");
		$assert(js.lang.Types.isNumber(height), "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Height argument is not a number.");
		$assert(typeof xoffset !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Xoffset argument is undefined.");
		$assert(js.lang.Types.isNumber(xoffset), "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Xoffset argument is not a number.");
		$assert(typeof yoffset !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Yoffset argument is undefined.");
		$assert(js.lang.Types.isNumber(yoffset), "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Yoffset argument is not a number.");

		var __callback__ = arguments[5];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Callback is not a function.");
		var __scope__ = arguments[6];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#cropCircleImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "cropCircleImage");
		rmi.setParameters(image, width, height, xoffset, yoffset);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Adjust brightness contrast.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param int brightness,
	 * @param int contrast,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 adjustBrightnessContrast: function(image, brightness, contrast) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#adjustBrightnessContrast", "Image argument is undefined.");
		$assert(typeof brightness !== "undefined", "com.kidscademy.atlas.studio.ImageService#adjustBrightnessContrast", "Brightness argument is undefined.");
		$assert(js.lang.Types.isNumber(brightness), "com.kidscademy.atlas.studio.ImageService#adjustBrightnessContrast", "Brightness argument is not a number.");
		$assert(typeof contrast !== "undefined", "com.kidscademy.atlas.studio.ImageService#adjustBrightnessContrast", "Contrast argument is undefined.");
		$assert(js.lang.Types.isNumber(contrast), "com.kidscademy.atlas.studio.ImageService#adjustBrightnessContrast", "Contrast argument is not a number.");

		var __callback__ = arguments[3];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#adjustBrightnessContrast", "Callback is not a function.");
		var __scope__ = arguments[4];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#adjustBrightnessContrast", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "adjustBrightnessContrast");
		rmi.setParameters(image, brightness, contrast);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Undo image.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 undoImage: function(image) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#undoImage", "Image argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#undoImage", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#undoImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "undoImage");
		rmi.setParameters(image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Commit image.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 commitImage: function(image) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#commitImage", "Image argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#commitImage", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#commitImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "commitImage");
		rmi.setParameters(image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Rollback image.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 rollbackImage: function(image) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#rollbackImage", "Image argument is undefined.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#rollbackImage", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#rollbackImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "rollbackImage");
		rmi.setParameters(image);
		rmi.exec(__callback__, __scope__);
	}
};

if(typeof ImageService === 'undefined') {
	ImageService = com.kidscademy.atlas.studio.ImageService;
}
