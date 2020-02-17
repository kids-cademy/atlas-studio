WinMain.on("load", function() {
	const description1="\
<text>\
	<section name='lorem'>\
		<h1>Lorem ipsum dolor</h1>\
		<p>Lorem ipsum dolor don't <em>sit amet</em>, <strong>consectetur</strong> adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>\
		<p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>\
		<ul>\
			<li>Cras mollis <em>scelerisque</em> nunc.</li>\
			<li>Nullam arcu.</li>\
			<li><strong>Aliquam</strong> consequat.</li>\
		</ul>\
		<p>Nullam varius, turpis et commodo pharetra, est eros bibendum elit, nec luctus magna felis sollicitudin mauris.</p>\
	</section>\
	<section name='aenean'>\
		<h1>Aenean magna nisl</h1>\
		<p>Aenean magna nisl, mollis quis, molestie eu, feugiat in, orci. In hac habitasse platea dictumst.</p>\
		<ol>\
			<li>Curabitur pretium tincidunt lacus.</li>\
			<li>Nulla gravida orci a odio.</li>\
		</ol>\
		<p>Curabitur augue lorem, dapibus quis, laoreet et, pretium ac, nisi.</p>\
		<h2>Cras mollis scelerisque nunc</h2>\
		<p>Aenean magna nisl, mollis quis, molestie eu, feugiat in, orci. In hac habitasse platea dictumst.</p>\
	</section>\
	<section name='curabitur'>\
		<h1><em>Curabitur pretium tincidunt</em></h1>\
		<p>Nullam varius, turpis et commodo pharetra, est eros bibendum elit, nec luctus magna felis sollicitudin mauris.</p>\
		<p>Integer in mauris eu nibh euismod gravida. Duis ac tellus et risus vulputate vehicula.</p>\
		<p>Donec lobortis risus a elit. Etiam tempor.</p>\
		<p>Ut ullamcorper, ligula eu tempor congue, eros est euismod turpis, id tincidunt sapien risus a quam. Maecenas fermentum consequat mi.</p>\
	</section>\
</text>"; 

const description2="\
<text>\
	<section name='text'>\
		<h1>Lorem ipsum dolor</h1>\
		<p>Lorem ipsum dolor don't <em>sit amet</em>, <strong>consectetur</strong> adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>\
		<p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>\
		<ul>\
			<li>Cras mollis <em>scelerisque</em> nunc.</li>\
			<li>Nullam arcu.</li>\
			<li><strong>Aliquam</strong> consequat.</li>\
		</ul>\
		<p>Nullam varius, turpis et commodo pharetra, est eros bibendum elit, nec luctus magna felis sollicitudin mauris.</p>\
		<h1>Aenean magna nisl</h1>\
		<p>Aenean magna nisl, mollis quis, molestie eu, feugiat in, orci. In hac habitasse platea dictumst.</p>\
		<ol>\
			<li>Curabitur pretium tincidunt lacus.</li>\
			<li>Nulla gravida orci a odio.</li>\
		</ol>\
		<p>Curabitur augue lorem, dapibus quis, laoreet et, pretium ac, nisi.</p>\
		<h2>Cras mollis scelerisque nunc</h2>\
		<p>Aenean magna nisl, mollis quis, molestie eu, feugiat in, orci. In hac habitasse platea dictumst.</p>\
		<h1><em>Curabitur pretium tincidunt</em></h1>\
		<p>Nullam varius, turpis et commodo pharetra, est eros bibendum elit, nec luctus magna felis sollicitudin mauris.</p>\
		<p>Integer in mauris eu nibh euismod gravida. Duis ac tellus et risus vulputate vehicula.</p>\
		<p>Donec lobortis risus a elit. Etiam tempor.</p>\
		<p>Ut ullamcorper, ligula eu tempor congue, eros est euismod turpis, id tincidunt sapien risus a quam. Maecenas fermentum consequat mi.</p>\
	</section>\
</text>"; 
	
	const form = WinMain.doc.getByTag("form");
	form.setObject({ description : description1 });
	
	const button = WinMain.doc.getByTag("button");
	button.on("click", function(ev) {
		alert(WinMain.doc.getByClass(com.kidscademy.form.DescriptionControl).getValue());
	});
});
