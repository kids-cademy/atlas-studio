$package("com.kidscademy.atlas.studio");

/**
 * Atlas service.
 */
com.kidscademy.atlas.studio.AtlasService = {
	/**
	 * Get collection items.
	 *
	 * @param java.lang.String collectionName,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasItem>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getCollectionItems: function(collectionName) {
		$assert(typeof collectionName !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionItems", "Collection name argument is undefined.");
		$assert(collectionName === null || js.lang.Types.isString(collectionName), "com.kidscademy.atlas.studio.AtlasService#getCollectionItems", "Collection name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getCollectionItems", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getCollectionItems", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getCollectionItems");
		rmi.setParameters(collectionName);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get atlas object.
	 *
	 * @param int AtlasObjectId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AtlasObject
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getAtlasObject: function(AtlasObjectId) {
		$assert(typeof AtlasObjectId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getAtlasObject", "Atlas object id argument is undefined.");
		$assert(js.lang.Types.isNumber(AtlasObjectId), "com.kidscademy.atlas.studio.AtlasService#getAtlasObject", "Atlas object id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getAtlasObject", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getAtlasObject", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getAtlasObject");
		rmi.setParameters(AtlasObjectId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get atlas object by name.
	 *
	 * @param java.lang.String name,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AtlasObject
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getAtlasObjectByName: function(name) {
		$assert(typeof name !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getAtlasObjectByName", "Name argument is undefined.");
		$assert(name === null || js.lang.Types.isString(name), "com.kidscademy.atlas.studio.AtlasService#getAtlasObjectByName", "Name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getAtlasObjectByName", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getAtlasObjectByName", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getAtlasObjectByName");
		rmi.setParameters(name);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Save atlas object.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasObject AtlasObject,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AtlasObject
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 saveAtlasObject: function(AtlasObject) {
		$assert(typeof AtlasObject !== "undefined", "com.kidscademy.atlas.studio.AtlasService#saveAtlasObject", "Atlas object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#saveAtlasObject", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#saveAtlasObject", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "saveAtlasObject");
		rmi.setParameters(AtlasObject);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get related atlas objects.
	 *
	 * @param java.util.List<java.lang.String> names,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasItem>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getRelatedAtlasObjects: function(names) {
		$assert(typeof names !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getRelatedAtlasObjects", "Names argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getRelatedAtlasObjects", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getRelatedAtlasObjects", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getRelatedAtlasObjects");
		rmi.setParameters(names);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get available atlas objects.
	 *
	 * @param java.lang.String category,
	 * @param java.util.List<com.kidscademy.atlas.studio.model.AtlasItem> related,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasItem>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getAvailableAtlasObjects: function(category, related) {
		$assert(typeof category !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getAvailableAtlasObjects", "Category argument is undefined.");
		$assert(category === null || js.lang.Types.isString(category), "com.kidscademy.atlas.studio.AtlasService#getAvailableAtlasObjects", "Category argument is not a string.");
		$assert(typeof related !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getAvailableAtlasObjects", "Related argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getAvailableAtlasObjects", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getAvailableAtlasObjects", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getAvailableAtlasObjects");
		rmi.setParameters(category, related);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Create link.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Link
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 createLink: function(link) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.AtlasService#createLink", "Link argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#createLink", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#createLink", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "createLink");
		rmi.setParameters(link);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Import object definition.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.lang.String
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 importObjectDefinition: function(link) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.AtlasService#importObjectDefinition", "Link argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#importObjectDefinition", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#importObjectDefinition", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "importObjectDefinition");
		rmi.setParameters(link);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Import object description.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.lang.String
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 importObjectDescription: function(link) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.AtlasService#importObjectDescription", "Link argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#importObjectDescription", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#importObjectDescription", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "importObjectDescription");
		rmi.setParameters(link);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Import objects facts.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 importObjectsFacts: function(link) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.AtlasService#importObjectsFacts", "Link argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#importObjectsFacts", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#importObjectsFacts", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "importObjectsFacts");
		rmi.setParameters(link);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Upload picture.
	 *
	 * @param js.http.form.Form form,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @throws js.rmi.BusinessException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 uploadPicture: function(form) {
		$assert(typeof form !== "undefined", "com.kidscademy.atlas.studio.AtlasService#uploadPicture", "Form argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#uploadPicture", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#uploadPicture", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "uploadPicture");
		rmi.setParameters(form);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Upload picture by source.
	 *
	 * @param js.http.form.Form form,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @throws js.rmi.BusinessException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 uploadPictureBySource: function(form) {
		$assert(typeof form !== "undefined", "com.kidscademy.atlas.studio.AtlasService#uploadPictureBySource", "Form argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#uploadPictureBySource", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#uploadPictureBySource", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "uploadPictureBySource");
		rmi.setParameters(form);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Duplicate picture.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image picture,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 duplicatePicture: function(object, picture) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#duplicatePicture", "Object argument is undefined.");
		$assert(typeof picture !== "undefined", "com.kidscademy.atlas.studio.AtlasService#duplicatePicture", "Picture argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#duplicatePicture", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#duplicatePicture", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "duplicatePicture");
		rmi.setParameters(object, picture);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Trim picture.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image picture,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 trimPicture: function(object, picture) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#trimPicture", "Object argument is undefined.");
		$assert(typeof picture !== "undefined", "com.kidscademy.atlas.studio.AtlasService#trimPicture", "Picture argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#trimPicture", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#trimPicture", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "trimPicture");
		rmi.setParameters(object, picture);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Flop picture.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image picture,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 flopPicture: function(object, picture) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#flopPicture", "Object argument is undefined.");
		$assert(typeof picture !== "undefined", "com.kidscademy.atlas.studio.AtlasService#flopPicture", "Picture argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#flopPicture", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#flopPicture", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "flopPicture");
		rmi.setParameters(object, picture);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Flip picture.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image picture,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 flipPicture: function(object, picture) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#flipPicture", "Object argument is undefined.");
		$assert(typeof picture !== "undefined", "com.kidscademy.atlas.studio.AtlasService#flipPicture", "Picture argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#flipPicture", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#flipPicture", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "flipPicture");
		rmi.setParameters(object, picture);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Crop picture.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image picture,
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
	 cropPicture: function(object, picture, width, height, xoffset, yoffset) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Object argument is undefined.");
		$assert(typeof picture !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Picture argument is undefined.");
		$assert(typeof width !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Width argument is undefined.");
		$assert(js.lang.Types.isNumber(width), "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Width argument is not a number.");
		$assert(typeof height !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Height argument is undefined.");
		$assert(js.lang.Types.isNumber(height), "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Height argument is not a number.");
		$assert(typeof xoffset !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Xoffset argument is undefined.");
		$assert(js.lang.Types.isNumber(xoffset), "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Xoffset argument is not a number.");
		$assert(typeof yoffset !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Yoffset argument is undefined.");
		$assert(js.lang.Types.isNumber(yoffset), "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Yoffset argument is not a number.");

		var __callback__ = arguments[6];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Callback is not a function.");
		var __scope__ = arguments[7];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#cropPicture", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "cropPicture");
		rmi.setParameters(object, picture, width, height, xoffset, yoffset);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Remove picture.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image picture,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 removePicture: function(object, picture) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#removePicture", "Object argument is undefined.");
		$assert(typeof picture !== "undefined", "com.kidscademy.atlas.studio.AtlasService#removePicture", "Picture argument is undefined.");

		var __callback__ = arguments[2];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#removePicture", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#removePicture", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "removePicture");
		rmi.setParameters(object, picture);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Undo picture.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image picture,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 undoPicture: function(object, picture) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#undoPicture", "Object argument is undefined.");
		$assert(typeof picture !== "undefined", "com.kidscademy.atlas.studio.AtlasService#undoPicture", "Picture argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#undoPicture", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#undoPicture", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "undoPicture");
		rmi.setParameters(object, picture);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Commit picture.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image picture,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 commitPicture: function(object, picture) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#commitPicture", "Object argument is undefined.");
		$assert(typeof picture !== "undefined", "com.kidscademy.atlas.studio.AtlasService#commitPicture", "Picture argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#commitPicture", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#commitPicture", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "commitPicture");
		rmi.setParameters(object, picture);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Rollback picture.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image picture,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 rollbackPicture: function(object, picture) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#rollbackPicture", "Object argument is undefined.");
		$assert(typeof picture !== "undefined", "com.kidscademy.atlas.studio.AtlasService#rollbackPicture", "Picture argument is undefined.");

		var __callback__ = arguments[2];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#rollbackPicture", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#rollbackPicture", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "rollbackPicture");
		rmi.setParameters(object, picture);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Upload audio sample.
	 *
	 * @param js.http.form.Form form,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.tool.AudioSampleInfo
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 uploadAudioSample: function(form) {
		$assert(typeof form !== "undefined", "com.kidscademy.atlas.studio.AtlasService#uploadAudioSample", "Form argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#uploadAudioSample", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#uploadAudioSample", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "uploadAudioSample");
		rmi.setParameters(form);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Normalize audio sample.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.tool.AudioSampleInfo
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 normalizeAudioSample: function(object) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#normalizeAudioSample", "Object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#normalizeAudioSample", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#normalizeAudioSample", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "normalizeAudioSample");
		rmi.setParameters(object);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Convert audio sample to mono.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.tool.AudioSampleInfo
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 convertAudioSampleToMono: function(object) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#convertAudioSampleToMono", "Object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#convertAudioSampleToMono", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#convertAudioSampleToMono", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "convertAudioSampleToMono");
		rmi.setParameters(object);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Trim audio sample silence.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.tool.AudioSampleInfo
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 trimAudioSampleSilence: function(object) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#trimAudioSampleSilence", "Object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#trimAudioSampleSilence", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#trimAudioSampleSilence", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "trimAudioSampleSilence");
		rmi.setParameters(object);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Cut audio sample.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param float start,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.tool.AudioSampleInfo
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 cutAudioSample: function(object, start) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cutAudioSample", "Object argument is undefined.");
		$assert(typeof start !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cutAudioSample", "Start argument is undefined.");
		$assert(js.lang.Types.isNumber(start), "com.kidscademy.atlas.studio.AtlasService#cutAudioSample", "Start argument is not a number.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#cutAudioSample", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#cutAudioSample", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "cutAudioSample");
		rmi.setParameters(object, start);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Fade in audio sample.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.tool.AudioSampleInfo
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 fadeInAudioSample: function(object) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#fadeInAudioSample", "Object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#fadeInAudioSample", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#fadeInAudioSample", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "fadeInAudioSample");
		rmi.setParameters(object);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Fade out audio sample.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.tool.AudioSampleInfo
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 fadeOutAudioSample: function(object) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#fadeOutAudioSample", "Object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#fadeOutAudioSample", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#fadeOutAudioSample", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "fadeOutAudioSample");
		rmi.setParameters(object);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Generate waveform.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.MediaSRC
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 generateWaveform: function(object) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#generateWaveform", "Object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#generateWaveform", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#generateWaveform", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "generateWaveform");
		rmi.setParameters(object);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Undo audio sample processing.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.tool.AudioSampleInfo
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 undoAudioSampleProcessing: function(object) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#undoAudioSampleProcessing", "Object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#undoAudioSampleProcessing", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#undoAudioSampleProcessing", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "undoAudioSampleProcessing");
		rmi.setParameters(object);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Rollback audio sample processing.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.tool.AudioSampleInfo
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 rollbackAudioSampleProcessing: function(object) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#rollbackAudioSampleProcessing", "Object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#rollbackAudioSampleProcessing", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#rollbackAudioSampleProcessing", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "rollbackAudioSampleProcessing");
		rmi.setParameters(object);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Remove audio sample.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 removeAudioSample: function(object) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#removeAudioSample", "Object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#removeAudioSample", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#removeAudioSample", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "removeAudioSample");
		rmi.setParameters(object);
		rmi.exec(__callback__, __scope__);
	}
};

if(typeof AtlasService === 'undefined') {
	AtlasService = com.kidscademy.atlas.studio.AtlasService;
}
