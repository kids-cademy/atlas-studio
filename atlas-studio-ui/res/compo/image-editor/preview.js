WinMain.on("load", () => {
	const object = {
		id: 1234,	
		collection: {
			name: "test-collection"
		},
		name: "test-object"
	};
	
	const image = {
		fileName: "contextual.jpg",
		fileSize: 109661,
		width: 1112,
		height: 674
	};

	const imageEditor = WinMain.doc.getByClass(com.kidscademy.ImageEditor);
	imageEditor.config({
		aspectRatio: 1.6
	});

	
	const metaForm = WinMain.doc.getByClass(com.kidscademy.FormData);
	
	const imageView = WinMain.doc.getByCssClass("image");
	imageView.on("click", ev => {
		metaForm.open();
		image.src = ev.target.getAttr("src"); 
		imageEditor.open(image, image => {
			imageView.reload(image.src);
		});
	});
	
	imageEditor.on("open", () => {
		metaForm.open();
	});
	
	imageEditor.on("close", image => {
		metaForm.hide();
	});

	imageEditor.on("upload", handler => {
        const formData = metaForm.getFormData();
        formData.append("atlas-object-id", object.id);
        formData.append("media-file", handler.file);

        AtlasService.uploadImage(formData, image => {
			imageView.reload(image.src);
			handler.callback(image);
        });
	});
	
	imageEditor.on("link", handler => {
        if (!metaForm.isValid()) {
            return;
        }
        const formData = metaForm.getFormData();
        formData.append("atlas-object-id", object.id);

        AtlasService.uploadImageBySource(formData, image => {
			imageView.reload(image.src);
			handler.callback(image);
        });
    });
	
	imageEditor.on("change", image => {
		metaForm.setObject(image);
	});
	
	imageEditor.on("remove", image => {
//        ImageService.removeImage(object, image, () => {
    		imageView.remove();
//        });
	})
});
