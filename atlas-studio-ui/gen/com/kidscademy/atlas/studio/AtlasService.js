$package("com.kidscademy.atlas.studio");

/**
 * Atlas service.
 */
com.kidscademy.atlas.studio.AtlasService = {
	/**
	 * Create atlas collection.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AtlasCollection
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 createAtlasCollection: function() {
		var __callback__ = arguments[0];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#createAtlasCollection", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#createAtlasCollection", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "createAtlasCollection");
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Save atlas collection.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasCollection collection,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AtlasCollection
	 * @throws js.rmi.BusinessException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 saveAtlasCollection: function(collection) {
		$assert(typeof collection !== "undefined", "com.kidscademy.atlas.studio.AtlasService#saveAtlasCollection", "Collection argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#saveAtlasCollection", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#saveAtlasCollection", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "saveAtlasCollection");
		rmi.setParameters(collection);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Remove atlas collection.
	 *
	 * @param int collectionId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws js.rmi.BusinessException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 removeAtlasCollection: function(collectionId) {
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#removeAtlasCollection", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.AtlasService#removeAtlasCollection", "Collection id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#removeAtlasCollection", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#removeAtlasCollection", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "removeAtlasCollection");
		rmi.setParameters(collectionId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get collection.
	 *
	 * @param int collectionId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AtlasCollection
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getCollection: function(collectionId) {
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollection", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.AtlasService#getCollection", "Collection id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getCollection", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getCollection", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getCollection");
		rmi.setParameters(collectionId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get collections.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasCollection>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getCollections: function() {
		var __callback__ = arguments[0];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getCollections", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getCollections", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getCollections");
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get collection items.
	 *
	 * @param java.util.Map<java.lang.String,java.lang.String> filter,
	 * @param int collectionId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasItem>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getCollectionItems: function(filter, collectionId) {
		$assert(typeof filter !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionItems", "Filter argument is undefined.");
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionItems", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.AtlasService#getCollectionItems", "Collection id argument is not a number.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getCollectionItems", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getCollectionItems", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getCollectionItems");
		rmi.setParameters(filter, collectionId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get collection images.
	 *
	 * @param java.util.Map<java.lang.String,java.lang.String> filter,
	 * @param int collectionId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasImages>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getCollectionImages: function(filter, collectionId) {
		$assert(typeof filter !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionImages", "Filter argument is undefined.");
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionImages", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.AtlasService#getCollectionImages", "Collection id argument is not a number.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getCollectionImages", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getCollectionImages", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getCollectionImages");
		rmi.setParameters(filter, collectionId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get collection related.
	 *
	 * @param java.util.Map<java.lang.String,java.lang.String> filter,
	 * @param int collectionId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasRelated>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getCollectionRelated: function(filter, collectionId) {
		$assert(typeof filter !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionRelated", "Filter argument is undefined.");
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionRelated", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.AtlasService#getCollectionRelated", "Collection id argument is not a number.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getCollectionRelated", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getCollectionRelated", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getCollectionRelated");
		rmi.setParameters(filter, collectionId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get collection links.
	 *
	 * @param java.util.Map<java.lang.String,java.lang.String> filter,
	 * @param int collectionId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasLinks>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getCollectionLinks: function(filter, collectionId) {
		$assert(typeof filter !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionLinks", "Filter argument is undefined.");
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionLinks", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.AtlasService#getCollectionLinks", "Collection id argument is not a number.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getCollectionLinks", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getCollectionLinks", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getCollectionLinks");
		rmi.setParameters(filter, collectionId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get collection items by taxon.
	 *
	 * @param int collectionId,
	 * @param com.kidscademy.atlas.studio.model.Taxon taxon,
	 * @param java.util.List<com.kidscademy.atlas.studio.model.AtlasItem> excludes,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasItem>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getCollectionItemsByTaxon: function(collectionId, taxon, excludes) {
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionItemsByTaxon", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.AtlasService#getCollectionItemsByTaxon", "Collection id argument is not a number.");
		$assert(typeof taxon !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionItemsByTaxon", "Taxon argument is undefined.");
		$assert(typeof excludes !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getCollectionItemsByTaxon", "Excludes argument is undefined.");

		var __callback__ = arguments[3];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getCollectionItemsByTaxon", "Callback is not a function.");
		var __scope__ = arguments[4];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getCollectionItemsByTaxon", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getCollectionItemsByTaxon");
		rmi.setParameters(collectionId, taxon, excludes);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Create atlas object.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasCollection collection,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AtlasObject
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 createAtlasObject: function(collection) {
		$assert(typeof collection !== "undefined", "com.kidscademy.atlas.studio.AtlasService#createAtlasObject", "Collection argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#createAtlasObject", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#createAtlasObject", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "createAtlasObject");
		rmi.setParameters(collection);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get atlas object.
	 *
	 * @param int objectId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AtlasObject
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getAtlasObject: function(objectId) {
		$assert(typeof objectId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getAtlasObject", "Object id argument is undefined.");
		$assert(js.lang.Types.isNumber(objectId), "com.kidscademy.atlas.studio.AtlasService#getAtlasObject", "Object id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getAtlasObject", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getAtlasObject", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getAtlasObject");
		rmi.setParameters(objectId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Save atlas object.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasObject object,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AtlasObject
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 saveAtlasObject: function(object) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#saveAtlasObject", "Object argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#saveAtlasObject", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#saveAtlasObject", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "saveAtlasObject");
		rmi.setParameters(object);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Remove atlas object.
	 *
	 * @param int objectId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 removeAtlasObject: function(objectId) {
		$assert(typeof objectId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#removeAtlasObject", "Object id argument is undefined.");
		$assert(js.lang.Types.isNumber(objectId), "com.kidscademy.atlas.studio.AtlasService#removeAtlasObject", "Object id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#removeAtlasObject", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#removeAtlasObject", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "removeAtlasObject");
		rmi.setParameters(objectId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Move atlas object.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param int collectionId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 moveAtlasObject: function(object, collectionId) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#moveAtlasObject", "Object argument is undefined.");
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#moveAtlasObject", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.AtlasService#moveAtlasObject", "Collection id argument is not a number.");

		var __callback__ = arguments[2];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#moveAtlasObject", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#moveAtlasObject", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "moveAtlasObject");
		rmi.setParameters(object, collectionId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get related atlas objects.
	 *
	 * @param int collectionId,
	 * @param java.util.List<java.lang.String> relatedNames,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasItem>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getRelatedAtlasObjects: function(collectionId, relatedNames) {
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getRelatedAtlasObjects", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.AtlasService#getRelatedAtlasObjects", "Collection id argument is not a number.");
		$assert(typeof relatedNames !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getRelatedAtlasObjects", "Related names argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getRelatedAtlasObjects", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getRelatedAtlasObjects", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getRelatedAtlasObjects");
		rmi.setParameters(collectionId, relatedNames);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get feature templates.
	 *
	 * @param com.kidscademy.atlas.studio.model.FeaturesClass featuresClass,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.Feature>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getFeatureTemplates: function(featuresClass) {
		$assert(typeof featuresClass !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getFeatureTemplates", "Features class argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getFeatureTemplates", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getFeatureTemplates", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getFeatureTemplates");
		rmi.setParameters(featuresClass);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Create link.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Link
	 * @throws js.rmi.BusinessException
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
	 * Get link definition.
	 *
	 * @param java.net.URL link,
	 * @param java.lang.String objectDisplay,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.lang.String
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getLinkDefinition: function(link, objectDisplay) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getLinkDefinition", "Link argument is undefined.");
		$assert(link === null || js.lang.Types.isString(link), "com.kidscademy.atlas.studio.AtlasService#getLinkDefinition", "Link argument is not a string.");
		$assert(typeof objectDisplay !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getLinkDefinition", "Object display argument is undefined.");
		$assert(objectDisplay === null || js.lang.Types.isString(objectDisplay), "com.kidscademy.atlas.studio.AtlasService#getLinkDefinition", "Object display argument is not a string.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getLinkDefinition", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getLinkDefinition", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getLinkDefinition");
		rmi.setParameters(link, objectDisplay);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Create link meta.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.LinkMeta
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 createLinkMeta: function() {
		var __callback__ = arguments[0];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#createLinkMeta", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#createLinkMeta", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "createLinkMeta");
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get links meta.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.LinkMeta>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getLinksMeta: function() {
		var __callback__ = arguments[0];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getLinksMeta", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getLinksMeta", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getLinksMeta");
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Save link meta.
	 *
	 * @param com.kidscademy.atlas.studio.model.LinkMeta linkMeta,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.LinkMeta
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 saveLinkMeta: function(linkMeta) {
		$assert(typeof linkMeta !== "undefined", "com.kidscademy.atlas.studio.AtlasService#saveLinkMeta", "Link meta argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#saveLinkMeta", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#saveLinkMeta", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "saveLinkMeta");
		rmi.setParameters(linkMeta);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Remove link meta.
	 *
	 * @param int linkMetaId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 removeLinkMeta: function(linkMetaId) {
		$assert(typeof linkMetaId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#removeLinkMeta", "Link meta id argument is undefined.");
		$assert(js.lang.Types.isNumber(linkMetaId), "com.kidscademy.atlas.studio.AtlasService#removeLinkMeta", "Link meta id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#removeLinkMeta", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#removeLinkMeta", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "removeLinkMeta");
		rmi.setParameters(linkMetaId);
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
	 * Import object facts.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 importObjectFacts: function(link) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.AtlasService#importObjectFacts", "Link argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#importObjectFacts", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#importObjectFacts", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "importObjectFacts");
		rmi.setParameters(link);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Import object features.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.Feature>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 importObjectFeatures: function(link) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.AtlasService#importObjectFeatures", "Link argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#importObjectFeatures", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#importObjectFeatures", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "importObjectFeatures");
		rmi.setParameters(link);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Import wikipedia object.
	 *
	 * @param int collectionId,
	 * @param java.net.URL articleURL,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AtlasItem
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 importWikipediaObject: function(collectionId, articleURL) {
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#importWikipediaObject", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.AtlasService#importWikipediaObject", "Collection id argument is not a number.");
		$assert(typeof articleURL !== "undefined", "com.kidscademy.atlas.studio.AtlasService#importWikipediaObject", "Article URL argument is undefined.");
		$assert(articleURL === null || js.lang.Types.isString(articleURL), "com.kidscademy.atlas.studio.AtlasService#importWikipediaObject", "Article URL argument is not a string.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#importWikipediaObject", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#importWikipediaObject", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "importWikipediaObject");
		rmi.setParameters(collectionId, articleURL);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Load atlas object taxonomy.
	 *
	 * @param java.lang.String objectName,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.Taxon>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 loadAtlasObjectTaxonomy: function(objectName) {
		$assert(typeof objectName !== "undefined", "com.kidscademy.atlas.studio.AtlasService#loadAtlasObjectTaxonomy", "Object name argument is undefined.");
		$assert(objectName === null || js.lang.Types.isString(objectName), "com.kidscademy.atlas.studio.AtlasService#loadAtlasObjectTaxonomy", "Object name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#loadAtlasObjectTaxonomy", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#loadAtlasObjectTaxonomy", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "loadAtlasObjectTaxonomy");
		rmi.setParameters(objectName);
		rmi.exec(__callback__, __scope__);
	},

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
		$assert(typeof form !== "undefined", "com.kidscademy.atlas.studio.AtlasService#uploadImage", "Form argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#uploadImage", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#uploadImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "uploadImage");
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
		$assert(typeof form !== "undefined", "com.kidscademy.atlas.studio.AtlasService#uploadImageBySource", "Form argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#uploadImageBySource", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#uploadImageBySource", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "uploadImageBySource");
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
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cloneImageToIcon", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cloneImageToIcon", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#cloneImageToIcon", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#cloneImageToIcon", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "cloneImageToIcon");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Trim image.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 trimImage: function(object, image) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#trimImage", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#trimImage", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#trimImage", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#trimImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "trimImage");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Flop image.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 flopImage: function(object, image) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#flopImage", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#flopImage", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#flopImage", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#flopImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "flopImage");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Flip image.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 flipImage: function(object, image) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#flipImage", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#flipImage", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#flipImage", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#flipImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "flipImage");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Rotate image left.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 rotateImageLeft: function(object, image) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#rotateImageLeft", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#rotateImageLeft", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#rotateImageLeft", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#rotateImageLeft", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "rotateImageLeft");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Rotate image right.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 rotateImageRight: function(object, image) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#rotateImageRight", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#rotateImageRight", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#rotateImageRight", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#rotateImageRight", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "rotateImageRight");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Crop image.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
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
	 cropImage: function(object, image, width, height, xoffset, yoffset) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropImage", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropImage", "Image argument is undefined.");
		$assert(typeof width !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropImage", "Width argument is undefined.");
		$assert(js.lang.Types.isNumber(width), "com.kidscademy.atlas.studio.AtlasService#cropImage", "Width argument is not a number.");
		$assert(typeof height !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropImage", "Height argument is undefined.");
		$assert(js.lang.Types.isNumber(height), "com.kidscademy.atlas.studio.AtlasService#cropImage", "Height argument is not a number.");
		$assert(typeof xoffset !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropImage", "Xoffset argument is undefined.");
		$assert(js.lang.Types.isNumber(xoffset), "com.kidscademy.atlas.studio.AtlasService#cropImage", "Xoffset argument is not a number.");
		$assert(typeof yoffset !== "undefined", "com.kidscademy.atlas.studio.AtlasService#cropImage", "Yoffset argument is undefined.");
		$assert(js.lang.Types.isNumber(yoffset), "com.kidscademy.atlas.studio.AtlasService#cropImage", "Yoffset argument is not a number.");

		var __callback__ = arguments[6];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#cropImage", "Callback is not a function.");
		var __scope__ = arguments[7];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#cropImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "cropImage");
		rmi.setParameters(object, image, width, height, xoffset, yoffset);
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
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#removeImage", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#removeImage", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#removeImage", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#removeImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "removeImage");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Undo image.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 undoImage: function(object, image) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#undoImage", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#undoImage", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#undoImage", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#undoImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "undoImage");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Commit image.
	 *
	 * @param com.kidscademy.atlas.studio.model.AtlasItem object,
	 * @param com.kidscademy.atlas.studio.model.Image image,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 commitImage: function(object, image) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#commitImage", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#commitImage", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#commitImage", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#commitImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "commitImage");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Rollback image.
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
	 rollbackImage: function(object, image) {
		$assert(typeof object !== "undefined", "com.kidscademy.atlas.studio.AtlasService#rollbackImage", "Object argument is undefined.");
		$assert(typeof image !== "undefined", "com.kidscademy.atlas.studio.AtlasService#rollbackImage", "Image argument is undefined.");

		var __callback__ = arguments[2];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#rollbackImage", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#rollbackImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "rollbackImage");
		rmi.setParameters(object, image);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Upload audio sample.
	 *
	 * @param js.tiny.container.http.form.Form form,
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
	},

	/**
	 * Update index.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.lang.NoSuchMethodException
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 updateIndex: function() {
		var __callback__ = arguments[0];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#updateIndex", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#updateIndex", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "updateIndex");
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Search.
	 *
	 * @param java.lang.String criterion,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.Set<com.kidscademy.atlas.studio.model.AtlasItem>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 search: function(criterion) {
		$assert(typeof criterion !== "undefined", "com.kidscademy.atlas.studio.AtlasService#search", "Criterion argument is undefined.");
		$assert(criterion === null || js.lang.Types.isString(criterion), "com.kidscademy.atlas.studio.AtlasService#search", "Criterion argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#search", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#search", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "search");
		rmi.setParameters(criterion);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get wiki how title.
	 *
	 * @param java.net.URL url,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.lang.String
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getWikiHowTitle: function(url) {
		$assert(typeof url !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getWikiHowTitle", "Url argument is undefined.");
		$assert(url === null || js.lang.Types.isString(url), "com.kidscademy.atlas.studio.AtlasService#getWikiHowTitle", "Url argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getWikiHowTitle", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getWikiHowTitle", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getWikiHowTitle");
		rmi.setParameters(url);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get export object.
	 *
	 * @param int objectId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.export.ExportObject
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getExportObject: function(objectId) {
		$assert(typeof objectId !== "undefined", "com.kidscademy.atlas.studio.AtlasService#getExportObject", "Object id argument is undefined.");
		$assert(js.lang.Types.isNumber(objectId), "com.kidscademy.atlas.studio.AtlasService#getExportObject", "Object id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#getExportObject", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#getExportObject", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "getExportObject");
		rmi.setParameters(objectId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Update feature display.
	 *
	 * @param com.kidscademy.atlas.studio.model.Feature feature,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Feature
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 updateFeatureDisplay: function(feature) {
		$assert(typeof feature !== "undefined", "com.kidscademy.atlas.studio.AtlasService#updateFeatureDisplay", "Feature argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.AtlasService#updateFeatureDisplay", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.AtlasService#updateFeatureDisplay", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.AtlasService", "updateFeatureDisplay");
		rmi.setParameters(feature);
		rmi.exec(__callback__, __scope__);
	}
};

if(typeof AtlasService === 'undefined') {
	AtlasService = com.kidscademy.atlas.studio.AtlasService;
}
