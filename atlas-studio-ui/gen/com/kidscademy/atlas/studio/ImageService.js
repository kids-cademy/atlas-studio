$package("com.kidscademy.atlas.studio");

/**
 * Image service.
 */
com.kidscademy.atlas.studio.ImageService = {
	/**
	 * Upload image.
	 *
	 * @param js.tiny.container.http.form.Form form,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @throws js.rmi.BusinessException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 uploadImage: function(form) {
		$assert(typeof form !== "undefined", "com.kidscademy.atlas.studio.ImageService#uploadImage", "Form argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#uploadImage", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#uploadImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "uploadImage");
		rmi.setParameters(form);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Upload image by source.
	 *
	 * @param js.tiny.container.http.form.Form form,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @throws js.rmi.BusinessException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 uploadImageBySource: function(form) {
		$assert(typeof form !== "undefined", "com.kidscademy.atlas.studio.ImageService#uploadImageBySource", "Form argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#uploadImageBySource", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#uploadImageBySource", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "uploadImageBySource");
		rmi.setParameters(form);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Clone image to icon.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 cloneImageToIcon: function(object, image) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.ImageService#cloneImageToIcon", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#cloneImageToIcon", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#cloneImageToIcon", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#cloneImageToIcon", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "cloneImageToIcon");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Remove image.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 removeImage: function(object, image) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.ImageService#removeImage", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#removeImage", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#removeImage", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#removeImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "removeImage");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

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
	 * Rotate image left.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 rotateImageLeft: function(image) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#rotateImageLeft", "Image argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#rotateImageLeft", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#rotateImageLeft", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "rotateImageLeft");
		rmi.setParameters(image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Rotate image right.
	 *
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 rotateImageRight: function(image) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#rotateImageRight", "Image argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#rotateImageRight", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#rotateImageRight", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "rotateImageRight");
		rmi.setParameters(image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Crop image.
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
	 cropImage: function(image, width, height, xoffset, yoffset) {
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropImage", "Image argument is undefined.");
		$assert(typeof width !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropImage", "Width argument is undefined.");
		$assert(js.lang.Types.isNumber(width), "com.kidscademy.atlas.studio.ImageService#cropImage", "Width argument is not a number.");
		$assert(typeof height !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropImage", "Height argument is undefined.");
		$assert(js.lang.Types.isNumber(height), "com.kidscademy.atlas.studio.ImageService#cropImage", "Height argument is not a number.");
		$assert(typeof xoffset !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropImage", "Xoffset argument is undefined.");
		$assert(js.lang.Types.isNumber(xoffset), "com.kidscademy.atlas.studio.ImageService#cropImage", "Xoffset argument is not a number.");
		$assert(typeof yoffset !== "undefined", "com.kidscademy.atlas.studio.ImageService#cropImage", "Yoffset argument is undefined.");
		$assert(js.lang.Types.isNumber(yoffset), "com.kidscademy.atlas.studio.ImageService#cropImage", "Yoffset argument is not a number.");

		var __callback__ = arguments[5];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ImageService#cropImage", "Callback is not a function.");
		var __scope__ = arguments[6];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ImageService#cropImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ImageService", "cropImage");
		rmi.setParameters(image, width, height, xoffset, yoffset);
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
